package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de campanha entrada")
public record CampanhaEntrada(
        @NotNull(message = "O parceiro é obrigatório.")
        @Schema(description = "Identificador relacionado a parceiroId", example = "1") Long parceiroId,

        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 160, message = "O título deve possuir no máximo 160 caracteres.")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data de início é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataFim,

        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String imagemUrl
) {
}
