package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Favorito;

@Schema(description = "Schema de favorito response")
public record FavoritoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a atrativoId", example = "1") Long atrativoId,
        String atrativoNome,
        String atrativoResumo,
        @Schema(description = "Identificador relacionado a categoriaAtrativoId", example = "1") Long categoriaAtrativoId,
        String categoriaAtrativoNome,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataCadastro
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
