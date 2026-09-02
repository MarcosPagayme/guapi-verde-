package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.enums.TipoConsentimento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Schema de consentimento entrada")
public record ConsentimentoEntrada(
        @NotNull(message = "O tipo de consentimento é obrigatório.")
        @Schema(description = "Tipo de consentimento", example = "POLITICA_PRIVACIDADE") TipoConsentimento tipo,

        @NotBlank(message = "A versão do termo é obrigatória.")
        @Size(max = 30, message = "A versão do termo deve possuir no máximo 30 caracteres.")
        String versaoTermo,

        @NotNull(message = "A decisão de consentimento é obrigatória.")
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean consentido
) {
}
