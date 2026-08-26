package com.GuapiVerde.mvp.dto;

import com.GuapiVerde.mvp.enums.PerfilUsuario;

public record LoginResponse (
    String token,
    String tipo,
    Long usuarioId,
    String nome,
    String email,
    PerfilUsuario perfil

){
}
