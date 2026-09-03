package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Cupom;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Schema de cupom response")
public record CupomResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        @Schema(description = "Identificador relacionado a campanhaId", example = "1") Long campanhaId,
        String campanhaTitulo,
        @Schema(description = "Identificador relacionado a parceiroId", example = "1") Long parceiroId,
        String parceiroNome,
        @Schema(description = "Código do cupom", example = "GUAPIVERDE10") String codigo,
        String descricao,
        String regrasUso,
        @JsonFormat(pattern = "yyyy-MM-dd") @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataValidade,
        Integer quantidadeDisponivel,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo
) {

    public static CupomResponse de(Cupom cupom) {
        Campanha campanha = cupom.getCampanha();
        Parceiro parceiro = campanha.getParceiro();

        return new CupomResponse(
                cupom.getId(),
                campanha.getId(),
                campanha.getTitulo(),
                parceiro.getId(),
                parceiro.getNome(),
                cupom.getCodigo(),
                cupom.getDescricao(),
                cupom.getRegrasUso(),
                cupom.getDataValidade(),
                cupom.getQuantidadeDisponivel(),
                cupom.getAtivo()
        );
    }
}
