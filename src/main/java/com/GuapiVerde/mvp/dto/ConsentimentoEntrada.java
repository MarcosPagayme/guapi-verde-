package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.enums.TipoConsentimento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConsentimentoEntrada(
        @NotNull(message = "O tipo de consentimento é obrigatório.")
        TipoConsentimento tipo,

        @NotBlank(message = "A versão do termo é obrigatória.")
        @Size(max = 30, message = "A versão do termo deve possuir no máximo 30 caracteres.")
        String versaoTermo,

        @NotNull(message = "A decisão de consentimento é obrigatória.")
        Boolean consentido
) {
}
