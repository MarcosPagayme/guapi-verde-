package com.GuapiVerde.mvp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.NovidadeEntrada;
import com.GuapiVerde.mvp.dto.NovidadeResponse;
import com.GuapiVerde.mvp.service.NovidadeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/novidades")
@RequiredArgsConstructor
public class NovidadeController {

    private final NovidadeService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NovidadeResponse> listarPublicadas() {
        return service.listarPublicadas();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<NovidadeResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/administracao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NovidadeResponse obterParaAdministracaoPorId(@PathVariable Long id) {
        return service.obterParaAdministracaoPorId(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NovidadeResponse obterPublicadaPorId(@PathVariable Long id) {
        return service.obterPublicadaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NovidadeResponse cadastrar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody NovidadeEntrada entrada) {
        return service.cadastrar(jwt.getSubject(), entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NovidadeResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NovidadeEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/publicar")
    @ResponseStatus(HttpStatus.OK)
    public NovidadeResponse publicar(@PathVariable Long id) {
        return service.publicar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void arquivar(@PathVariable Long id) {
        service.arquivar(id);
    }
}
