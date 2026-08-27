package com.GuapiVerde.mvp.controller;

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

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginEntrada entrada) {
        return authService.login(entrada);
    }

    @GetMapping("/me")
    public CadastroUsuarioResponse obterUsuarioAutenticado(
            @AuthenticationPrincipal Jwt jwt) {
        return CadastroUsuarioResponse.de(
                usuarioService.buscarPorEmail(jwt.getSubject()));
    }
}
