package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Novidade;
import com.GuapiVerde.mvp.enums.SituacaoNovidade;

@Schema(description = "Schema de novidade response")
public record NovidadeResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String titulo,
        String resumo,
        String conteudo,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String imagemUrl,
        @Schema(description = "Situação da novidade", example = "RASCUNHO") SituacaoNovidade situacao,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataPublicacao,
        @Schema(description = "Identificador relacionado a autorId", example = "1") Long autorId,
        String autorNome,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataCadastro,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataAtualizacao
) {

    public static NovidadeResponse de(Novidade novidade) {
        return new NovidadeResponse(
                novidade.getId(),
                novidade.getTitulo(),
                novidade.getResumo(),
                novidade.getConteudo(),
                novidade.getImagemUrl(),
                novidade.getSituacao(),
                novidade.getDataPublicacao(),
                novidade.getAutor().getId(),
                novidade.getAutor().getNome(),
                novidade.getDataCadastro(),
                novidade.getDataAtualizacao()
        );
    }
}
