package com.GuapiVerde.mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.GuapiVerde.mvp.enums.PerfilUsuario;

@Schema(description = "Schema de login response")
public record LoginResponse (
    @Schema(description = "Token JWT retornado após a autenticação") String token,
    String tipo,
    @Schema(description = "Identificador relacionado a usuarioId", example = "1") Long usuarioId,
    String nome,
    String email,
    @Schema(description = "Perfil de acesso do usuário", example = "VISITANTE") PerfilUsuario perfil

){
}
