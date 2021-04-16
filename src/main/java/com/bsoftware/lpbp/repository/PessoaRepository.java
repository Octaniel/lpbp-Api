package com.bsoftware.lpbp.repository;


import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Turno;
import com.bsoftware.lpbp.repository.pessoa.PessoaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User: Octaniel
 * Date: 24/09/2020
 * Time: 06:02
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {
    Optional<Pessoa> findByCodigoEquals(String codigo);
    List<Pessoa> findAllByTurno(Turno turno);
}
