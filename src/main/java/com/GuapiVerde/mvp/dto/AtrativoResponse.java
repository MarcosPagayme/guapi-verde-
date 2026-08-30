package com.GuapiVerde.mvp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.GuapiVerde.mvp.entity.Atrativo;
import com.GuapiVerde.mvp.enums.SituacaoAtrativo;

public record AtrativoResponse(
        Long id,
        String nome,
        String resumo,
        String descricao,
        String endereco,
        BigDecimal latitude,
        BigDecimal longitude,
        String telefone,
        String site,
        Boolean gratuito,
        BigDecimal valorEntrada,
        Boolean acessivel,
        SituacaoAtrativo situacao,
        Boolean ativo,
        CategoriaAtrativoResumoResponse categoria,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
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
