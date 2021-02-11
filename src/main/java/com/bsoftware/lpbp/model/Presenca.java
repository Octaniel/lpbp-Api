package com.bsoftware.lpbp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User: Octaniel
 * Date: 06/10/2020
 * Time: 07:06
 */

@Entity
@Table(name = "presenca")
@Getter
@Setter
@EqualsAndHashCode
public class Presenca implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference("pessoa_presenca")
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    private Boolean presente;

    @Column(name = "url_foto")
    private String nomeFoto;

    private Boolean justificada;

    @Column(name = "aceito_gerente")
    private Boolean justificacaoAceitoPorGerente;

    @Column(name = "aceito_admin")
    private Boolean justificacaoAceitoPorAdministrador;

    @Column(name = "url_audio")
    private String nomeAudio;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;



    @Transient
    private String codigo;
}
