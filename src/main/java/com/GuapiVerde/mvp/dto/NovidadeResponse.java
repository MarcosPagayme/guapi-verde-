package com.GuapiVerde.mvp.dto;

import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Novidade;
import com.GuapiVerde.mvp.enums.SituacaoNovidade;

public record NovidadeResponse(
        Long id,
        String titulo,
        String resumo,
        String conteudo,
        String imagemUrl,
        SituacaoNovidade situacao,
        LocalDateTime dataPublicacao,
        Long autorId,
        String autorNome,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
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
