package com.bsoftware.lpbp.service;

import com.bsoftware.lpbp.event.RecursoCriadoEvent;
import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.model.dtos.Pessoadto;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.service.exeption.UsuarioException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;

@Service
public class PessoaService {
    private final ApplicationEventPublisher publisher;

//    private final JavaMailSender mailSender;

    private final PessoaRepository pessoaRepository;

    public PessoaService(ApplicationEventPublisher publisher,
                         PessoaRepository pessoaRepository) {
        this.publisher = publisher;
        this.pessoaRepository = pessoaRepository;
    }

    public void checkPersonByCode(String codigoPessoa){
        Optional<Pessoa> byCodigo = pessoaRepository.findByCodigoEquals(codigoPessoa);
        byCodigo.orElseThrow(() -> new UsuarioException("Nenhum funcionario existente"));
    }

    public boolean salvar(Pessoa pessoa) {
        validar(pessoa);
        String s;
        do {
            s = criarCodigo();
        } while (pessoaRepository.findByCodigo(s) != null);
        pessoa.setCodigo(s);

        pessoaRepository.save(pessoa);
        return true;
    }

    public boolean update(Pessoadto pessoa) {
        Pessoa pessoa1 = pessoaRepository.getOne(pessoa.getId());
        validar(pessoa.toPessoa(pessoa1));
        pessoaRepository.save(pessoa1);
        return true;
    }

    private void validar(Pessoa pessoa) {
        Pessoa pessoa1 = pessoaRepository.findByNomeAndApelido(pessoa.getNome(), pessoa.getApelido());
        if (pessoa1 != null)
            throw new UsuarioException("Já existe uma pessoa com mesmo nome e apelido");
    }

    private String criarCodigo() {
        Random random1 = new Random();
        return "" + (random1.nextInt(9) + 1) + (random1.nextInt(9) + 1) + (random1.nextInt(9) + 1) +
                (random1.nextInt(9) + 1) + (random1.nextInt(9) + 1);
    }
}
