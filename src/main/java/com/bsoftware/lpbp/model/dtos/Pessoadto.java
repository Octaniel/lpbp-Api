package com.bsoftware.lpbp.model.dtos;

import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Turno;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Pessoadto {

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

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    public Pessoa toPessoa(Pessoa pessoa) {
        pessoa.setNome(nome);
        pessoa.setApelido(apelido);
        pessoa.setEmail(email);
        pessoa.setMorada(morada);
        pessoa.setTelemovel(telemovel);
        pessoa.setDataNascimento(dataNascimento);
        pessoa.setTurno(turno);
        return pessoa;
    }
}
