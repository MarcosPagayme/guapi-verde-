package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;

public record CategoriaAtrativoResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo
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
