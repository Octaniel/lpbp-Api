package com.bsoftware.lpbp.repository;

import com.bsoftware.lpbp.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * User: Octaniel
 * Date: 06/10/2020
 * Time: 07:27
 */
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    List<Presenca> findAllByPessoaId(Long idPessoa);
    List<Presenca> findAllByValidado(Boolean validado);
}
