package com.bsoftware.lpbp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@EqualsAndHashCode
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatorio")
    private String nome;

    @NotBlank(message = "O apelido é obrigatorio")
    private String apelido;

    @NotBlank(message = "O email é obrigatorio")
    private String email;

    private String morada;

    @NotNull(message = "Telemovel é obrigatorio")
    private String telemovel;

    private String codigo;

    private LocalDate dataNascimento;

    @Column(name = "dt_cria")
    private LocalDateTime dataCriacao;

    @Column(name = "dt_alter")
    private LocalDateTime dataAlteracao;

    @JsonManagedReference("pessoa_presenca")
    @OneToMany(mappedBy = "pessoa",cascade = CascadeType.ALL)
    private List<Presenca> presencas;

    @PrePersist
    public void antesSalvar(){
        dataAlteracao = LocalDateTime.now();
        dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    public void antesAtualizar(){
        dataAlteracao = LocalDateTime.now();
    }
}
