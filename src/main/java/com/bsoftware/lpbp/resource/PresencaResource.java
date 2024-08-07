package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.repository.filter.PresencaFilter;
import com.bsoftware.lpbp.repository.projection.PresencaResumo;
import com.bsoftware.lpbp.service.PresencaService;
import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

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
    public Map<String, Boolean> salvar(String codigoPessoa, String urlFoto) {
        return Map.of("status", presencaService.salvar(codigoPessoa, urlFoto));
    }

    @PostMapping("offline")
    public ResponseEntity<Presenca> salvarOffline(String codigoPessoa, String urlFoto, String date, boolean presente, HttpServletResponse httpServletResponse) {
        return presencaService.salvarOffline(codigoPessoa, urlFoto, date, presente, httpServletResponse);
    }

    @GetMapping
    public List<PresencaResumo> listar(PresencaFilter presencaFilter) {
        return presencaRepository.geralList(presencaFilter);
    }

    @GetMapping("{id}")
    public Presenca listarPorId(@PathVariable Long id) {
        return presencaRepository.findById(id).orElse(null);
    }

    @GetMapping("idPessoa/{id}")
    public List<Presenca> listarPorIdPessoa(@PathVariable Long id) {
        return presencaRepository.findAllByPessoaId(id);
    }

    @PatchMapping
    public Boolean adicionarUrlVideo(String urlVideo, Long id) {
        return presencaService.adicionarUrlVideo(urlVideo, id);
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
            LOGGER.log( Logger.Level.ERROR , e);
        }
    }
}
