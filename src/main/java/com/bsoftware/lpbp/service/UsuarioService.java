package com.bsoftware.lpbp.service;

import com.bsoftware.lpbp.event.RecursoCriadoEvent;
import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.repository.UsuarioRepository;
import com.bsoftware.lpbp.service.exeption.UsuarioException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {
    private final ApplicationEventPublisher publisher;

//    private final JavaMailSender mailSender;

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(ApplicationEventPublisher publisher, UsuarioRepository usuarioRepository) {
        this.publisher = publisher;
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<Usuario> salvar(Usuario usuario, HttpServletResponse httpServletResponse) {
        validar(usuario, 0L);
        LocalDateTime localDateTime = LocalDateTime.now();
        usuario.setDataAlteracao(localDateTime);
        usuario.setDataCriacao(localDateTime);

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        usuario.setSenha(encoder.encode(usuario.getSenha()));

        usuario.getPessoa().setCodigo(criarCodigo());

        Usuario save = usuarioRepository.save(usuario);
//        Usuario one = usuarioRepository.getOne(save.getId());
        publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, save.getId()));
        //String s = enviarEmail(one.getCliente().getPessoa().getEmail(), one.getCliente().getPessoa().getCodigo());
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    private void validar(Usuario usuario, Long id) {
//        if (!usuario.getSenha().equals(usuario.getConfirmacaoSenha()))
//            throw new UsuarioException("A Senha e a confirmação de senha são diferentes");
        List<Usuario> all = usuarioRepository.findAll();
        all.forEach(x -> {
            if (usuario.getNome().equals(x.getNome()) && !x.getId().equals(id))
                throw new UsuarioException("Este nome já esta sendo utilizado por outro utilizador");

        });
    }

//    private String enviarEmail(String email, String senhaTemporaria) {
//        try {
//            MimeMessage mail = mailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(mail);
//            helper.setTo(email);
//            helper.setSubject("Codigo de e-dobra");
//            helper.setText("<p>Codigo:" + senhaTemporaria + "</p>" +
//                    "<p>Guarda isto muito bem, esse codigo só pertence a você</p>", true);
//            mailSender.send(mail);
//
//            return "OK";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Erro ao enviar e-mail";
//        }
//    }

    private String criarSenha() {
        RandomString random = new RandomString();
        Random random1 = new Random();
        String s = random.nextString();
        String senha = (random1.nextInt(9) + 1) + "" + s.charAt(1) + "" + (random1.nextInt(9) + 1) + "" + s.charAt(3);
        senha += (random1.nextInt(9) + 1) + "" + s.charAt(1) + "" + (random1.nextInt(9) + 1) + "" + s.charAt(3);
        return senha;
    }

    private String criarCodigo() {
        Random random1 = new Random();
        return ""+(random1.nextInt(9)+1)+(random1.nextInt(9)+1)+(random1.nextInt(9)+1)+
                (random1.nextInt(9)+1)+(random1.nextInt(9)+1);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        validar(usuario, id);
        Optional<Usuario> byId = usuarioRepository.findById(id);
        assert byId.orElse(null) != null;
        BeanUtils.copyProperties(usuario, byId.orElse(null), "id", "dataCriacao", "senha", "senhaTemporaria", "usuarioCriouId", "empresa");
        byId.get().setDataAlteracao(LocalDateTime.now());
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        byId.get().setSenha(encoder.encode(usuario.getSenha()));
        return usuarioRepository.save(byId.get());
    }
}
