package com.GuapiVerde.mvp.dto;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Temporada;
import com.fasterxml.jackson.annotation.JsonFormat;

public record TemporadaResponse(
        Long id,
        String nome,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataFim,
        Boolean ativo
) {

    public static TemporadaResponse de(Temporada temporada) {
        return new TemporadaResponse(
                temporada.getId(),
                temporada.getNome(),
                temporada.getDescricao(),
                temporada.getDataInicio(),
                temporada.getDataFim(),
                temporada.getAtivo()
        );
    }
}
