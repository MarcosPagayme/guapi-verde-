package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.enums.PerfilUsuario;

public record CadastroUsuarioResponse(
    Long Id,
    String nome,
    String email,
    PerfilUsuario perfil,
    Boolean ativo,
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
