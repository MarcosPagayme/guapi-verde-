package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;

public record CategoriaAtrativoResposta(
        Long id,
        String nome,
        String descricao,
        Boolean ativo
) {

    public static CategoriaAtrativoResposta de(CategoriaAtrativo categoria) {
        return new CategoriaAtrativoResposta(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getAtivo()
        );
    }
}
