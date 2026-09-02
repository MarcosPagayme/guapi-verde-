package com.GuapiVerde.mvp.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CampanhaEntrada(
        @NotNull(message = "O parceiro é obrigatório.")
        Long parceiroId,

        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 160, message = "O título deve possuir no máximo 160 caracteres.")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "A data de início é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataFim,

        @Size(max = 500, message = "A URL da imagem deve possuir no máximo 500 caracteres.")
        String imagemUrl
) {
}
