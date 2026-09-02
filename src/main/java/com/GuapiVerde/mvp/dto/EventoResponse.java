package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.Evento;
import com.GuapiVerde.mvp.entity.Temporada;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Schema de evento response")
public record EventoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome,
        String resumo,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataHoraInicio,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataHoraFim,
        String local,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String imagemUrl,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo,
        @Schema(description = "Identificador relacionado a atrativoId", example = "1") Long atrativoId,
        String atrativoNome,
        @Schema(description = "Identificador relacionado a temporadaId", example = "1") Long temporadaId,
        String temporadaNome
) {

    public static EventoResponse de(Evento evento) {
        Atrativo atrativo = evento.getAtrativo();
        Temporada temporada = evento.getTemporada();

        return new EventoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getResumo(),
                evento.getDescricao(),
                evento.getDataHoraInicio(),
                evento.getDataHoraFim(),
                evento.getLocal(),
                evento.getImagemUrl(),
                evento.getAtivo(),
                atrativo == null ? null : atrativo.getId(),
                atrativo == null ? null : atrativo.getNome(),
                temporada == null ? null : temporada.getId(),
                temporada == null ? null : temporada.getNome()
        );
    }
}
