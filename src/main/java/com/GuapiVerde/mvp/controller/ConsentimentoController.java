package com.GuapiVerde.mvp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.ConsentimentoEntrada;
import com.GuapiVerde.mvp.dto.ConsentimentoResponse;
import com.GuapiVerde.mvp.service.ConsentimentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consentimentos")
@RequiredArgsConstructor
public class ConsentimentoController {

    private final ConsentimentoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConsentimentoResponse> listarHistorico(@AuthenticationPrincipal Jwt jwt) {
        return service.listarHistoricoDoUsuario(jwt.getSubject());
    }

    @GetMapping("/atuais")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsentimentoResponse> listarAtuais(@AuthenticationPrincipal Jwt jwt) {
        return service.listarAtuaisDoUsuario(jwt.getSubject());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsentimentoResponse registrar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ConsentimentoEntrada entrada) {
        return service.registrar(jwt.getSubject(), entrada);
    }
}
