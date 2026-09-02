package com.GuapiVerde.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NovidadeEntrada(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 180, message = "O título deve possuir no máximo 180 caracteres.")
        String titulo,

        @NotBlank(message = "O resumo é obrigatório.")
        @Size(max = 300, message = "O resumo deve possuir no máximo 300 caracteres.")
        String resumo,

        @NotBlank(message = "O conteúdo é obrigatório.")
        String conteudo,

        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        String imagemUrl
) {
}
