package com.GuapiVerde.mvp.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HorarioFuncionamentoEntrada(
        @NotNull(message = "O atrativo é obrigatório.")
        @Positive(message = "O ID do atrativo deve ser positivo.")
        Long atrativoId,

        @NotBlank(message = "O dia da semana é obrigatório.")
        @Size(max = 20, message = "O dia da semana deve possuir no máximo 20 caracteres.")
        String diaSemana,

        @JsonFormat(pattern = "HH:mm")
        LocalTime horarioAbertura,

        @JsonFormat(pattern = "HH:mm")
        LocalTime horarioFechamento,

        @NotNull(message = "Informe se o atrativo está fechado neste dia.")
        Boolean fechado,

        @Size(max = 255, message = "A observação deve possuir no máximo 255 caracteres.")
        String observacao
) {
}
