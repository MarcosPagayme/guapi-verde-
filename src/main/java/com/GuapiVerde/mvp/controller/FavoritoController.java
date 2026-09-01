package com.GuapiVerde.mvp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.FavoritoResponse;
import com.GuapiVerde.mvp.service.FavoritoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FavoritoResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return service.listarDoUsuario(jwt.getSubject());
    }

    @PostMapping("/{atrativoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoResponse adicionar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long atrativoId) {
        return service.adicionar(jwt.getSubject(), atrativoId);
    }

    @DeleteMapping("/{atrativoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long atrativoId) {
        service.remover(jwt.getSubject(), atrativoId);
    }
}
