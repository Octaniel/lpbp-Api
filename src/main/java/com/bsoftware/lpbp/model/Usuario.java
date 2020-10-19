package com.bsoftware.lpbp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@EqualsAndHashCode
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do usuario é obrigatorio")
    private String nome;

    private String senha;

    @NotNull(message = "Tens que selecionar um banco")
    @JoinColumn(name = "id_pessoa")
    @OneToOne
    private Pessoa pessoa;

    @Transient
    private String confirmacaoSenha;

    @Size(min = 1, message = "Pelo menos um grupo deve ser selecionado para o usuario")
    @NotNull(message = "Pelo menos um grupo deve ser selecionado para o usuario")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id")
            , inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<Grupo> grupos = new ArrayList<>();

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @Transient
    private String grupo;
}
