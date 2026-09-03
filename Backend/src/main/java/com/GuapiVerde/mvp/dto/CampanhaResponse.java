package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Schema de campanha response")
public record CampanhaResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a parceiroId", example = "1") Long parceiroId,
        String parceiroNome,
        String parceiroSite,
        String titulo,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd") @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataInicio,
        @JsonFormat(pattern = "yyyy-MM-dd") @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataFim,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String imagemUrl,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo
) {

    public static CampanhaResponse de(Campanha campanha) {
        Parceiro parceiro = campanha.getParceiro();

        return new CampanhaResponse(
                campanha.getId(),
                parceiro.getId(),
                parceiro.getNome(),
                parceiro.getSite(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.getImagemUrl(),
                campanha.getAtivo()
        );
    }
}
