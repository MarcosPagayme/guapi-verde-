package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Favorito;

public record FavoritoResponse(
        Long id,
        Long atrativoId,
        String atrativoNome,
        String atrativoResumo,
        Long categoriaAtrativoId,
        String categoriaAtrativoNome,
        LocalDateTime dataCadastro
) {

    public static FavoritoResponse de(Favorito favorito) {
        Atrativo atrativo = favorito.getAtrativo();
        CategoriaAtrativo categoria = atrativo.getCategoria();

        return new FavoritoResponse(
                favorito.getId(),
                atrativo.getId(),
                atrativo.getNome(),
                atrativo.getResumo(),
                categoria.getId(),
                categoria.getNome(),
                favorito.getDataCadastro()
        );
    }
}
