package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.service.PresencaService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("presenca")
public class PresencaResource {
    private final PresencaService presencaService;

    public PresencaResource(PresencaService presencaService) {
        this.presencaService = presencaService;
    }

    @PostMapping
    public ResponseEntity<Presenca> salvar(@RequestBody Presenca presenca, HttpServletResponse httpServletResponse) {
        return presencaService.salvar(presenca, httpServletResponse);
    }

    @PutMapping
    public Presenca atualizar(@RequestBody Presenca presenca) {
        return presencaService.atualizar(presenca);
    }

    @PostMapping("test")
    public void salvar(MultipartFile multipartFile) {
        File file1 = null;
        try {
            file1 = ResourceUtils.getFile("classpath:docs/saida.jpg");
            multipartFile.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
