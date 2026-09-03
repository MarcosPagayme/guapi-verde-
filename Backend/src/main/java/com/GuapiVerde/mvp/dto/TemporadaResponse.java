package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import com.GuapiVerde.mvp.entity.Temporada;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Schema de temporada response")
public record TemporadaResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd") @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataInicio,
        @JsonFormat(pattern = "yyyy-MM-dd") @Schema(description = "Data no formato AAAA-MM-DD", type = "string", format = "date", example = "2026-09-15") LocalDate dataFim,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo
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
