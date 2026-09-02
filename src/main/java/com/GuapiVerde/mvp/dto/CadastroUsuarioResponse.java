package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;

@Schema(description = "Schema de cadastro usuario response")
public record CadastroUsuarioResponse(
    @Schema(description = "Identificador relacionado a Id", example = "1") Long Id,
    String nome,
    String email,
    @Schema(description = "Perfil de acesso do usuário", example = "VISITANTE") PerfilUsuario perfil,
    @Schema(description = "Indicador verdadeiro ou falso", example = "true") Boolean ativo,
    String dataCadastro
){
    public static CadastroUsuarioResponse de(Usuario usuario) {
        return new CadastroUsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getAtivo(),
            usuario.getDataCadastro().toString()
        );
    }
}
