package com.GuapiVerde.mvp.dto;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Cupom;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CupomResponse(
        Long id,
        Long campanhaId,
        String campanhaTitulo,
        Long parceiroId,
        String parceiroNome,
        String codigo,
        String descricao,
        String regrasUso,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataValidade,
        Integer quantidadeDisponivel,
        Boolean ativo
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
