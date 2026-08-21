package com.GuapiVerde.mvp.controller;

import com.GuapiVerde.mvp.dto.CategoriaAtrativoEntrada;
import com.GuapiVerde.mvp.dto.CategoriaAtrativoResposta;
import com.GuapiVerde.mvp.service.CategoriaAtrativoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias-atrativos")
@RequiredArgsConstructor
public class CategoriaAtrativoController {

    private final CategoriaAtrativoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaAtrativoResposta cadastrar(
            @Valid @RequestBody CategoriaAtrativoEntrada entrada) {
        return service.cadastrar(entrada);
    }
}