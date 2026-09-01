package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.CategoriaAtrativo;
import com.GuapiVerde.mvp.entity.Preferencia;

public record PreferenciaResponse(
        Long id,
        Long categoriaAtrativoId,
        String categoriaAtrativoNome,
        String categoriaAtrativoDescricao,
        LocalDateTime dataCadastro
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
