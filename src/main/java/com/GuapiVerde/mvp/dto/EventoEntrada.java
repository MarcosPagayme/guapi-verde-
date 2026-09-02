package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EventoEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O resumo é obrigatório.")
        @Size(max = 300, message = "O resumo deve possuir no máximo 300 caracteres.")
        String resumo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data e hora de início são obrigatórias.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataHoraInicio,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataHoraFim,

        @Size(max = 255, message = "O local deve possuir no máximo 255 caracteres.")
        String local,

        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        String imagemUrl,

        Long atrativoId,
        Long temporadaId
) {
}
