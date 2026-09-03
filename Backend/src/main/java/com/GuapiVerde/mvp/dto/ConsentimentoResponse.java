package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Consentimento;
import com.GuapiVerde.mvp.enums.TipoConsentimento;

@Schema(description = "Schema de consentimento response")
public record ConsentimentoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Tipo de consentimento", example = "POLITICA_PRIVACIDADE") TipoConsentimento tipo,
        String versaoTermo,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean consentido,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataRegistro
) {

    public static ConsentimentoResponse de(Consentimento consentimento) {
        return new ConsentimentoResponse(
                consentimento.getId(),
                consentimento.getTipo(),
                consentimento.getVersaoTermo(),
                consentimento.getConsentido(),
                consentimento.getDataRegistro()
        );
    }
}
