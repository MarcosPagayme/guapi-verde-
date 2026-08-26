package com.GuapiVerde.mvp.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.GuapiVerde.mvp.dto.LoginEntrada;
import com.GuapiVerde.mvp.dto.LoginResponse;
import com.GuapiVerde.mvp.entity.Usuario;
import com.GuapiVerde.mvp.repository.UsuarioRepository;
import com.GuapiVerde.mvp.security.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public LoginResponse login(LoginEntrada entrada) {
        String email = entrada.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        var credenciais = new UsernamePasswordAuthenticationToken(email, entrada.senha());

        authenticationManager.authenticate(credenciais);

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow();

        String token = tokenService.gerarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil());
    }
}
