package com.bsoftware.lpbp.repository;

import com.bsoftware.lpbp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByPessoaEmail(String email);

}
