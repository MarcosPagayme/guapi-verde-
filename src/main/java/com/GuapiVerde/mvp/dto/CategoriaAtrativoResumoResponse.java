package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;

@Schema(description = "Schema de categoria atrativo resumo response")
public record CategoriaAtrativoResumoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome
) {

    public static CategoriaAtrativoResumoResponse de(CategoriaAtrativo categoria) {
        return new CategoriaAtrativoResumoResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }
}
