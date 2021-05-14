package com.bsoftware.lpbp.repository.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresencaResumo {
    //["Nome", "Faltas", "Faltas Justificadas", "Faltas Aceitas", "Presenças"]
    private String nome;
    private long falta;
    private long faltaJustificada;
    private long faltaJustificadaAceitas;
    private long presenca;

    public PresencaResumo(String nome, long falta, long faltaJustificada, long faltaJustificadaAceitas, long presenca) {
        this.nome = nome;
        this.falta = falta;
        this.faltaJustificada = faltaJustificada;
        this.faltaJustificadaAceitas = faltaJustificadaAceitas;
        this.presenca = presenca;
    }
}
