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

import com.GuapiVerde.mvp.dto.PreferenciaResponse;
import com.GuapiVerde.mvp.service.PreferenciaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/preferencias")
@RequiredArgsConstructor
public class PreferenciaController {

    private final PreferenciaService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PreferenciaResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return service.listarDoUsuario(jwt.getSubject());
    }

    @PostMapping("/{categoriaAtrativoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PreferenciaResponse adicionar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoriaAtrativoId) {
        return service.adicionar(jwt.getSubject(), categoriaAtrativoId);
    }

    @DeleteMapping("/{categoriaAtrativoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoriaAtrativoId) {
        service.remover(jwt.getSubject(), categoriaAtrativoId);
    }
}
