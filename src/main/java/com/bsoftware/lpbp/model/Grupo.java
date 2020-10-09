package com.bsoftware.lpbp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "grupo")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "grupo_permissao", joinColumns = @JoinColumn(name = "grupo_id")
            , inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private List<Permissao> permisoes;
}
