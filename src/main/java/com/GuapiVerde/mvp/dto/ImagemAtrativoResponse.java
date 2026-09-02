package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.entity.ImagemAtrativo;

@Schema(description = "Schema de imagem atrativo response")
public record ImagemAtrativoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a atrativoId", example = "1") Long atrativoId,
        String atrativoNome,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String url,
        String textoAlternativo,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean principal,
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
