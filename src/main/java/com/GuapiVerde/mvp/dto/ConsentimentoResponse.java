package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Consentimento;
import com.GuapiVerde.mvp.enums.TipoConsentimento;

public record ConsentimentoResponse(
        Long id,
        TipoConsentimento tipo,
        String versaoTermo,
        Boolean consentido,
        LocalDateTime dataRegistro
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
