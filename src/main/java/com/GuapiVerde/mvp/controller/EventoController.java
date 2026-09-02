package com.GuapiVerde.mvp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.EventoEntrada;
import com.GuapiVerde.mvp.dto.EventoResponse;
import com.GuapiVerde.mvp.service.EventoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPublicados() {
        return service.listarPublicados();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/atrativo/{atrativoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorAtrativo(@PathVariable Long atrativoId) {
        return service.listarPorAtrativo(atrativoId);
    }

    @GetMapping("/temporada/{temporadaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorTemporada(@PathVariable Long temporadaId) {
        return service.listarPorTemporada(temporadaId);
    }

    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {
        return service.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse obterPublicadoPorId(@PathVariable Long id) {
        return service.obterPublicadoPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse cadastrar(@Valid @RequestBody EventoEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
