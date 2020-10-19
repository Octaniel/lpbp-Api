package com.bsoftware.lpbp.service;

import com.bsoftware.lpbp.event.RecursoCriadoEvent;
import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.service.exeption.UsuarioException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PresencaService {
    private final ApplicationEventPublisher publisher;
    private final PresencaRepository presencaRepository;
    private final PessoaRepository pessoaRepository;

    public PresencaService(ApplicationEventPublisher publisher, PresencaRepository presencaRepository, PessoaRepository pessoaRepository) {
        this.publisher = publisher;
        this.presencaRepository = presencaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public ResponseEntity<Presenca> salvar(Presenca presenca, HttpServletResponse httpServletResponse) {
        Optional<Pessoa> byCodigo = pessoaRepository.findByCodigoEquals(presenca.getCodigo());
        byCodigo.orElseThrow(() -> new UsuarioException("Nenhum funcionario existente"));
        presenca.setPessoa(byCodigo.get());
        presenca.setPresente(true);
        LocalDateTime now = LocalDateTime.now();
        presenca.setDataAlteracao(now);
        presenca.setDataCriacao(now);
        Presenca save = presencaRepository.save(presenca);
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public Presenca atualizar(Presenca presenca){
        Optional<Presenca> byId = presencaRepository.findById(presenca.getId());
        BeanUtils.copyProperties(presenca,byId.get(),"pessoa","dataCriacao");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return presencaRepository.save(byId.get());
    }
}
