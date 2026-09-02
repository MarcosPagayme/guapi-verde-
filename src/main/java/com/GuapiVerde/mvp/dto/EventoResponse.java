package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.entity.Evento;
import com.GuapiVerde.mvp.entity.Temporada;
import com.fasterxml.jackson.annotation.JsonFormat;

public record EventoResponse(
        Long id,
        String nome,
        String resumo,
        String descricao,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dataHoraInicio,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dataHoraFim,
        String local,
        String imagemUrl,
        Boolean ativo,
        Long atrativoId,
        String atrativoNome,
        Long temporadaId,
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
