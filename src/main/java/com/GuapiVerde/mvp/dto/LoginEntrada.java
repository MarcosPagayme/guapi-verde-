package com.GuapiVerde.mvp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginEntrada(

    @NotBlank(message = "O campo email é obrigatório")
    @Email(message = "O campo email deve ser um endereço de email válido")
    String email,
    @NotBlank(message = "O campo senha é obrigatório")
    String senha
){
}
