package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Preferencia;

@Schema(description = "Schema de preferencia response")
public record PreferenciaResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a categoriaAtrativoId", example = "1") Long categoriaAtrativoId,
        String categoriaAtrativoNome,
        String categoriaAtrativoDescricao,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataCadastro
) {

    public static PreferenciaResponse de(Preferencia preferencia) {
        CategoriaAtrativo categoria = preferencia.getCategoriaAtrativo();

        return new PreferenciaResponse(
                preferencia.getId(),
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                preferencia.getDataCadastro()
        );
    }
}
