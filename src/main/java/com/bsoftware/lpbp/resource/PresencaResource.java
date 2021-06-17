package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.repository.filter.PresencaFilter;
import com.bsoftware.lpbp.repository.projection.PresencaResumo;
import com.bsoftware.lpbp.service.PresencaService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("presenca")
public class PresencaResource {
    private final PresencaService presencaService;
    private final PresencaRepository presencaRepository;

    public PresencaResource(PresencaService presencaService, PresencaRepository presencaRepository) {
        this.presencaService = presencaService;
        this.presencaRepository = presencaRepository;
    }

    @PostMapping
    public ResponseEntity<Presenca> salvar(@RequestBody Presenca presenca, HttpServletResponse httpServletResponse) {
        return presencaService.salvar(presenca, httpServletResponse);
    }

    @PostMapping("offline")
    public ResponseEntity<Presenca> salvarOffline(@RequestBody Presenca presenca, HttpServletResponse httpServletResponse) {
        return presencaService.salvarOffline(presenca, httpServletResponse);
    }

    @GetMapping
    public List<PresencaResumo> listar(PresencaFilter presencaFilter) {
        return presencaRepository.geralList(presencaFilter);
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
