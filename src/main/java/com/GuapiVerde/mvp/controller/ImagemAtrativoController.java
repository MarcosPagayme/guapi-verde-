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

import com.GuapiVerde.mvp.dto.ImagemAtrativoEntrada;
import com.GuapiVerde.mvp.dto.ImagemAtrativoResponse;
import com.GuapiVerde.mvp.service.ImagemAtrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/imagens-atrativos")
@RequiredArgsConstructor
public class ImagemAtrativoController {

    private final ImagemAtrativoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ImagemAtrativoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/atrativo/{atrativoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ImagemAtrativoResponse> listarPorAtrativo(@PathVariable Long atrativoId) {
        return service.listarPorAtrativo(atrativoId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImagemAtrativoResponse obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImagemAtrativoResponse cadastrar(
            @Valid @RequestBody ImagemAtrativoEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImagemAtrativoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ImagemAtrativoEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
