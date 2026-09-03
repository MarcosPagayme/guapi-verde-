package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.entity.Parceiro;

@Schema(description = "Schema de parceiro response")
public record ParceiroResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome,
        String descricao,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String logoUrl,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String site,
        String email,
        String telefone,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo
) {

    public static ParceiroResponse de(Parceiro parceiro) {
        return new ParceiroResponse(
                parceiro.getId(),
                parceiro.getNome(),
                parceiro.getDescricao(),
                parceiro.getLogoUrl(),
                parceiro.getSite(),
                parceiro.getEmail(),
                parceiro.getTelefone(),
                parceiro.getAtivo()
        );
    }
}
