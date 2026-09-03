package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de novidade entrada")
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
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String imagemUrl
) {
}
