package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.ImagemAtrativo;

public record ImagemAtrativoResponse(
        Long id,
        Long atrativoId,
        String atrativoNome,
        String url,
        String textoAlternativo,
        Boolean principal,
        Integer ordem
) {

    public static ImagemAtrativoResponse de(ImagemAtrativo imagem) {
        return new ImagemAtrativoResponse(
                imagem.getId(),
                imagem.getAtrativo().getId(),
                imagem.getAtrativo().getNome(),
                imagem.getUrl(),
                imagem.getTextoAlternativo(),
                imagem.getPrincipal(),
                imagem.getOrdem()
        );
    }
}
