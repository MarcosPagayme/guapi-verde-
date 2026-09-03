package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de imagem atrativo entrada")
public record ImagemAtrativoEntrada(
        @NotNull(message = "O atrativo é obrigatório.")
        @Positive(message = "O ID do atrativo deve ser positivo.")
        @Schema(description = "Identificador relacionado a atrativoId", example = "1") Long atrativoId,

        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String url,

        @NotBlank(message = "O texto alternativo é obrigatório.")
        @Size(max = 180, message = "O texto alternativo deve possuir no máximo 180 caracteres.")
        String textoAlternativo,

        @NotNull(message = "Informe se a imagem é principal.")
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean principal,

        @NotNull(message = "A ordem é obrigatória.")
        @PositiveOrZero(message = "A ordem não pode ser negativa.")
        Integer ordem
) {
}
