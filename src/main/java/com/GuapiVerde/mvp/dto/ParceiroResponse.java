package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.Parceiro;

public record ParceiroResponse(
        Long id,
        String nome,
        String descricao,
        String logoUrl,
        String site,
        String email,
        String telefone,
        Boolean ativo
) {

    public static ParceiroResponse de(Parceiro parceiro) {
        return new ParceiroResponse(
                parceiro.getId(),
                parceiro.getNome(),
                parceiro.getDescricao(),
                parceiro.getLogoUrl(),
                parceiro.getSite(),
                parceiro.getEmail(),
                parceiro.getTelefone(),
                parceiro.getAtivo()
        );
    }
}
