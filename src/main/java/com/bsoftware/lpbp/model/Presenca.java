package com.bsoftware.lpbp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    private Boolean presente;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;
}
