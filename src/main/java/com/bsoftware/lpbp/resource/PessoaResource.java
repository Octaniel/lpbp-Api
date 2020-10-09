package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.repository.PresencaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    public PessoaResource(PessoaRepository pessoaRepository, PresencaRepository presencaRepository) {
        this.pessoaRepository = pessoaRepository;
        this.presencaRepository = presencaRepository;
    }

    @GetMapping
    public List<Pessoa> listar() {
        List<Pessoa> all = pessoaRepository.findAll();
        all.forEach(x -> {
            List<Presenca> allByPessoaId = presencaRepository.findAllByPessoaId(x.getId());
            allByPessoaId.forEach(y-> y.setPessoa(null));
            x.setPresencas(allByPessoaId);
        });
        return all;
    }
}
