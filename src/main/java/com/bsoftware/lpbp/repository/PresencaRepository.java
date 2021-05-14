package com.bsoftware.lpbp.repository;

import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.repository.presenca.PresencaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: Octaniel
 * Date: 06/10/2020
 * Time: 07:27
 */
public interface PresencaRepository extends JpaRepository<Presenca, Long>, PresencaRepositoryQuery {
    List<Presenca> findAllByPessoaId(Long idPessoa);
    List<Presenca> findAllByValidado(Boolean validado);
    @Query("select p from Presenca p where p.dataCriacao>=?1 and p.dataCriacao<=?2")
    List<Presenca> presencasEntre(LocalDateTime de, LocalDateTime ate);
}
