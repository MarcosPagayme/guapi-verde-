package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;

@Schema(description = "Schema de categoria atrativo response")
public record CategoriaAtrativoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome,
        String descricao,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo
) {

    public static CategoriaAtrativoResponse de(CategoriaAtrativo categoria) {
        return new CategoriaAtrativoResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getAtivo()
        );
    }
}
