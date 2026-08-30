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

import com.GuapiVerde.mvp.dto.AtrativoEntrada;
import com.GuapiVerde.mvp.dto.AtrativoResponse;
import com.GuapiVerde.mvp.service.AtrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/atrativos")
@RequiredArgsConstructor
public class AtrativoController {

    private final AtrativoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AtrativoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AtrativoResponse> listarPorCategoria(@PathVariable Long categoriaId) {
        return service.listarPorCategoria(categoriaId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AtrativoResponse obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtrativoResponse cadastrar(@Valid @RequestBody AtrativoEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AtrativoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtrativoEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
