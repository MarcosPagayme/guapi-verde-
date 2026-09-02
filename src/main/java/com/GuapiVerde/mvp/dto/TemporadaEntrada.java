package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de temporada entrada")
public record TemporadaEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve possuir no máximo 120 caracteres.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data de início é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataFim
) {
}
