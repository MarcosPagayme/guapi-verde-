package com.GuapiVerde.mvp.dto;

import java.time.LocalTime;

import com.GuapiVerde.mvp.entity.HorarioFuncionamento;
import com.fasterxml.jackson.annotation.JsonFormat;

public record HorarioFuncionamentoResponse(
        Long id,
        Long atrativoId,
        String atrativoNome,
        String diaSemana,
        @JsonFormat(pattern = "HH:mm") LocalTime horarioAbertura,
        @JsonFormat(pattern = "HH:mm") LocalTime horarioFechamento,
        Boolean fechado,
        String observacao
) {

    public static HorarioFuncionamentoResponse de(HorarioFuncionamento horario) {
        return new HorarioFuncionamentoResponse(
                horario.getId(),
                horario.getAtrativo().getId(),
                horario.getAtrativo().getNome(),
                horario.getDiaSemana(),
                horario.getHorarioAbertura(),
                horario.getHorarioFechamento(),
                horario.getFechado(),
                horario.getObservacao()
        );
    }
}
