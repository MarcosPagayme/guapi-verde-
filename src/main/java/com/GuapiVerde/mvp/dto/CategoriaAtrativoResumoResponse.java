package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;

public record CategoriaAtrativoResumoResponse(
        Long id,
        String nome
) {

    public static CategoriaAtrativoResumoResponse de(CategoriaAtrativo categoria) {
        return new CategoriaAtrativoResumoResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }
}
