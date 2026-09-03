package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de categoria atrativo entrada")
public record CategoriaAtrativoEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 80, message = "O nome deve possuir no máximo 80 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.")
        String descricao
) {
}
