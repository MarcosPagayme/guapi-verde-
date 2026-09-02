package com.GuapiVerde.mvp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.TemporadaEntrada;
import com.GuapiVerde.mvp.dto.TemporadaResponse;
import com.GuapiVerde.mvp.service.TemporadaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/temporadas")
@RequiredArgsConstructor
public class TemporadaController {

    private final TemporadaService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TemporadaResponse> listarPublicadas() {
        return service.listarPublicadas();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<TemporadaResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse obterPublicadaPorId(@PathVariable Long id) {
        return service.obterPublicadaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemporadaResponse cadastrar(
            @Valid @RequestBody TemporadaEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TemporadaEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
