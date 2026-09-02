package com.GuapiVerde.mvp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ParceiroEntrada(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve possuir no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @Size(max = 500, message = "A URL do logo deve possuir no máximo 500 caracteres.")
        String logoUrl,

        @Size(max = 255, message = "O site deve possuir no máximo 255 caracteres.")
        String site,

        @Size(max = 150, message = "O e-mail deve possuir no máximo 150 caracteres.")
        @Email(message = "O e-mail deve possuir um formato válido.")
        String email,

        @Size(max = 30, message = "O telefone deve possuir no máximo 30 caracteres.")
        String telefone
) {
}
