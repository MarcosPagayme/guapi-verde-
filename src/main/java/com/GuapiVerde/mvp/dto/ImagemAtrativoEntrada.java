package com.GuapiVerde.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ImagemAtrativoEntrada(
        @NotNull(message = "O atrativo é obrigatório.")
        @Positive(message = "O ID do atrativo deve ser positivo.")
        Long atrativoId,

        @NotBlank(message = "A URL da imagem é obrigatória.")
        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        String url,

        @NotBlank(message = "O texto alternativo é obrigatório.")
        @Size(max = 180, message = "O texto alternativo deve possuir no máximo 180 caracteres.")
        String textoAlternativo,

        @NotNull(message = "Informe se a imagem é principal.")
        Boolean principal,

        @NotNull(message = "A ordem é obrigatória.")
        @PositiveOrZero(message = "A ordem não pode ser negativa.")
        Integer ordem
) {
}
