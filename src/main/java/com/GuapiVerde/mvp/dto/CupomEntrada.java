package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de cupom entrada")
public record CupomEntrada(
        @NotNull(message = "A campanha é obrigatória.")
        @Schema(description = "Identificador relacionado a campanhaId", example = "1") Long campanhaId,

        @NotBlank(message = "O código é obrigatório.")
        @Size(max = 60, message = "O código deve possuir no máximo 60 caracteres.")
        @Schema(description = "Código do cupom", example = "GUAPIVERDE10") String codigo,

        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 300, message = "A descrição deve possuir no máximo 300 caracteres.")
        String descricao,

        String regrasUso,

        @NotNull(message = "A data de validade é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataValidade,

        @PositiveOrZero(message = "A quantidade disponível deve ser maior ou igual a zero.")
        Integer quantidadeDisponivel
) {
}
