package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de cadastro usuario entrada")
public record CadastroUsuarioEntrada(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres") String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres") String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        @Schema(description = "Senha informada pelo usuário", accessMode = Schema.AccessMode.WRITE_ONLY, example = "SenhaSegura123") String senha
) {
}
