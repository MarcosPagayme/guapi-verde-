package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.enums.SituacaoAtrativo;

@Schema(description = "Schema de atrativo response")
public record AtrativoResponse(
        @Schema(description = "Identificador relacionado a id", example = "1") Long id,
        String nome,
        String resumo,
        String descricao,
        String endereco,
        BigDecimal latitude,
        BigDecimal longitude,
        String telefone,
        @Schema(description = "URL do recurso", format = "uri", example = "https://exemplo.com/imagem.jpg") String site,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean gratuito,
        BigDecimal valorEntrada,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean acessivel,
        @Schema(description = "Situação do atrativo") SituacaoAtrativo situacao,
        @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo,
        CategoriaAtrativoResumoResponse categoria,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataCadastro,
        @Schema(description = "Data e hora no padrão ISO 8601", type = "string", format = "date-time", example = "2026-09-15T09:00:00") LocalDateTime dataAtualizacao
) {

    public static AtrativoResponse de(Atrativo atrativo) {
        return new AtrativoResponse(
                atrativo.getId(),
                atrativo.getNome(),
                atrativo.getResumo(),
                atrativo.getDescricao(),
                atrativo.getEndereco(),
                atrativo.getLatitude(),
                atrativo.getLongitude(),
                atrativo.getTelefone(),
                atrativo.getSite(),
                atrativo.getGratuito(),
                atrativo.getValorEntrada(),
                atrativo.getAcessivel(),
                atrativo.getSituacao(),
                atrativo.getAtivo(),
                CategoriaAtrativoResumoResponse.de(atrativo.getCategoria()),
                atrativo.getDataCadastro(),
                atrativo.getDataAtualizacao()
        );
    }
}
