package com.bsoftware.lpbp.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadDTO {
    private MultipartFile file;
    private String codigo;
}
