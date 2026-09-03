package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

import com.GuapiVerde.mvp.entity.HorarioFuncionamento;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Schema de horario funcionamento response")
public record HorarioFuncionamentoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a atrativoId", example = "1") Long atrativoId,
        String atrativoNome,
        String diaSemana,
        @JsonFormat(pattern = "HH:mm") @Schema(description = "Horário no formato HH:mm", type = "string", example = "08:00") LocalTime horarioAbertura,
        @JsonFormat(pattern = "HH:mm") @Schema(description = "Horário no formato HH:mm", type = "string", example = "08:00") LocalTime horarioFechamento,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean fechado,
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
