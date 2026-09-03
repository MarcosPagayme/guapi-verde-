package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Schema de login entrada")
public record LoginEntrada(

    @NotBlank(message = "O campo email é obrigatório")
    @Email(message = "O campo email deve ser um endereço de email válido")
    String email,
    @NotBlank(message = "O campo senha é obrigatório")
    @Schema(description = "Senha informada pelo usuário", accessMode = Schema.AccessMode.WRITE_ONLY, example = "SenhaSegura123") String senha
){
}
