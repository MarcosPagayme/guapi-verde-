package com.GuapiVerde.mvp.dto;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Campanha;
import com.GuapiVerde.mvp.entity.Parceiro;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CampanhaResponse(
        Long id,
        Long parceiroId,
        String parceiroNome,
        String parceiroSite,
        String titulo,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataFim,
        String imagemUrl,
        Boolean ativo
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
