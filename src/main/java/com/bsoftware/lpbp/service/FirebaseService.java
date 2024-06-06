package com.bsoftware.lpbp.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {
    private final PresencaService presencaService;

    public FirebaseService(PresencaService presencaService) {
        this.presencaService = presencaService;
    }

    public String uploadFile(byte[] fileBytes, String fileName) {
        FirebaseApp app = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream serviceAccount = classloader.getResourceAsStream("docs/firebase-service-account.json")) {
            assert serviceAccount != null;
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("lpbp-smqi8q.appspot.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                app = FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert app != null;
        LocalDateTime now = LocalDateTime.now();
        String dateInt = now.getYear() + "" + now.getMonthValue() + now.getDayOfMonth() + now.getHour() + now.getMinute() + now.getSecond() + now.getNano();
        Storage storageClient = StorageClient.getInstance(app).bucket().getStorage();
        BlobInfo blobInfo = BlobInfo.newBuilder("lpbp-smqi8q.appspot.com", "images/" + dateInt + fileName)
                .setContentType("image/png")
                .build();
        storageClient.create(blobInfo, fileBytes);
        return "https://firebasestorage.googleapis.com/v0/b/lpbp-smqi8q.appspot.com/o/images%2F" + dateInt + fileName + "?alt=media&token=d17f61f6-90a5-4a7f-ba89-90d0fc8a07be";
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    @Async
    public CompletableFuture<String> upload(MultipartFile multipartFile, String codigo) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();
            presencaService.salvar(codigo, URL);
            return CompletableFuture.completedFuture(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("Image couldn't upload, Something went wrong");
        }
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("lpbp-smqi8q.appspot.com", fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("docs/firebase-service-account.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/lpbp-smqi8q.appspot.com/o/%s?alt=media&token=d17f61f6-90a5-4a7f-ba89-90d0fc8a07be";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
