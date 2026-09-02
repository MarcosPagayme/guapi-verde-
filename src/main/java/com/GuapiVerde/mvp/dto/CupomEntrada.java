package com.GuapiVerde.mvp.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CupomEntrada(
        @NotNull(message = "A campanha é obrigatória.")
        Long campanhaId,

        @NotBlank(message = "O código é obrigatório.")
        @Size(max = 60, message = "O código deve possuir no máximo 60 caracteres.")
        String codigo,

        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 300, message = "A descrição deve possuir no máximo 300 caracteres.")
        String descricao,

        String regrasUso,

        @NotNull(message = "A data de validade é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataValidade,

        @PositiveOrZero(message = "A quantidade disponível deve ser maior ou igual a zero.")
        Integer quantidadeDisponivel
) {
}
