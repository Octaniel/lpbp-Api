package com.bsoftware.lpbp.service;

import com.bsoftware.lpbp.event.RecursoCriadoEvent;
import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.model.Turno;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.service.exeption.UsuarioException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PresencaService {
    private final ApplicationEventPublisher publisher;
    private final PresencaRepository presencaRepository;
    private final PessoaRepository pessoaRepository;
    private List<Presenca> presencas = new ArrayList<>();

    public PresencaService(ApplicationEventPublisher publisher, PresencaRepository presencaRepository, PessoaRepository pessoaRepository) {
        this.publisher = publisher;
        this.presencaRepository = presencaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public Boolean salvar(String codigoPessoa, String urlFoto) {
        Optional<Pessoa> byCodigo = pessoaRepository.findByCodigoEquals(codigoPessoa);
        byCodigo.orElseThrow(() -> new UsuarioException("Nenhum funcionario existente"));
        Presenca presenca = new Presenca();
        presenca.setPessoa(byCodigo.get());
        presenca.setPresente(true);
        presenca.setNomeFoto(urlFoto);
        presenca.setValidado(true);
        presenca.setDataCriacao(LocalDateTime.now());
        presenca.setDataAlteracao(LocalDateTime.now());
        return getPresencaResponseEntity(presenca).getStatusCode().equals(HttpStatus.CREATED);
    }

    public ResponseEntity<Presenca> salvarOffline(String codigoPessoa, String urlFoto, String date, boolean presente, HttpServletResponse httpServletResponse) {
        Optional<Pessoa> byCodigo = pessoaRepository.findByCodigoEquals(codigoPessoa);
        byCodigo.orElseThrow(() -> new UsuarioException("Nenhum funcionario existente"));
        Presenca presenca = new Presenca();
        presenca.setPessoa(byCodigo.get());
        if (byCodigo.get().getTurno().equals(Turno.FERIA)) presenca.setPresente(true);
        presenca.setValidado(true);
        presenca.setPresente(presente);
        presenca.setNomeFoto(urlFoto);
        String[] split = date.split("-");
        LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
        presenca.setDataCriacao(dateTime);
        presenca.setDataAlteracao(dateTime);
        return getPresencaResponseEntity(presenca);
    }

    private ResponseEntity<Presenca> getPresencaResponseEntity(Presenca presenca) {
        presenca.setJustificada(false);
        presenca.setDataAlteracao(presenca.getDataCriacao());
        presencas.add(presenca);
        Presenca save = presencaRepository.save(presenca);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public Presenca atualizar(Presenca presenca) {
        Optional<Presenca> byId = presencaRepository.findById(presenca.getId());
        BeanUtils.copyProperties(presenca, byId.get(), "pessoa", "dataCriacao", "validado");
        byId.get().setDataAlteracao(LocalDateTime.now());
        return presencaRepository.save(byId.get());
    }

    public Boolean adicionarUrlVideo(String urlVideo, Long id) {

        Presenca presenca = presencaRepository.findById(id).orElse(null);
        if(presenca != null){
            presenca.setNomeAudio(urlVideo);
            presenca.setDataAlteracao(LocalDateTime.now());
            presencaRepository.save(presenca);
            return true;
        }
        return false;
    }
}
