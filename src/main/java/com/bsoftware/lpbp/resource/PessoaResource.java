package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.service.PessoaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * User: Octaniel
 * Date: 06/10/2020
 * Time: 07:02
 */
@RestController
@RequestMapping("pessoa")
public class PessoaResource {

    private final PessoaRepository pessoaRepository;
    private final PresencaRepository presencaRepository;
    private final PessoaService pessoaService;

    public PessoaResource(PessoaRepository pessoaRepository, PresencaRepository presencaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.presencaRepository = presencaRepository;
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public List<Pessoa> listar() {
        List<Pessoa> all = pessoaRepository.findAll();
        all.forEach(x -> {
            List<Presenca> allByPessoaId = presencaRepository.findAllByPessoaId(x.getId());
            allByPessoaId.forEach(y -> y.setPessoa(null));
            x.setPresencas(allByPessoaId);
        });
        return all;
    }

    @PostMapping
    public Map<String, Boolean> salvar(@Valid @RequestBody Pessoa pessoa) {
        return Map.of("status", pessoaService.salvar(pessoa));
    }
}
