package com.GuapiVerde.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaAtrativoEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 80, message = "O nome deve possuir no máximo 80 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.")
        String descricao
) {
}
