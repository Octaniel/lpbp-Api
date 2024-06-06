package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.service.FirebaseService;
import com.bsoftware.lpbp.service.PessoaService;
import com.bsoftware.lpbp.service.PresencaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
public class FileUploadController {

    private final FirebaseService firebaseService;
    private final PresencaService presencaService;
    private final PessoaService pessoaService;

    public FileUploadController(FirebaseService firebaseService, PresencaService presencaService, PessoaService pessoaService) {
        this.firebaseService = firebaseService;
        this.presencaService = presencaService;
        this.pessoaService = pessoaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file, String codigo) {
        pessoaService.checkPersonByCode(codigo);
        firebaseService.upload(file, codigo);
        return new ResponseEntity<>(Map.of("text", "Salvo Com Sucesso"), HttpStatus.OK);
    }
}
