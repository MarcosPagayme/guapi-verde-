package com.GuapiVerde.mvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.CadastroUsuarioResponse;
import com.GuapiVerde.mvp.dto.LoginEntrada;
import com.GuapiVerde.mvp.dto.LoginResponse;
import com.GuapiVerde.mvp.service.AuthService;
import com.GuapiVerde.mvp.service.UsuarioService;

import jakarta.validation.Valid;

@Tag(name = "Autenticação", description = "Cadastro, login e dados do usuário autenticado.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthService authService,
            UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }
    @Operation(summary = "Autenticar usuário", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticação realizada"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginEntrada entrada) {
        return authService.login(entrada);
    }
    @Operation(summary = "Consultar usuário autenticado", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/me")
    public CadastroUsuarioResponse obterUsuarioAutenticado(
            @AuthenticationPrincipal Jwt jwt) {
        return CadastroUsuarioResponse.de(
                usuarioService.buscarPorEmail(jwt.getSubject()));
    }
}
