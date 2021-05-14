package com.bsoftware.lpbp.repository;

import com.bsoftware.lpbp.model.Turno;
import com.bsoftware.lpbp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByPessoaEmail(String email);
    List<Usuario> findByPessoaTurnoAndTipo(Turno pessoa_turno, String tipo);

}
