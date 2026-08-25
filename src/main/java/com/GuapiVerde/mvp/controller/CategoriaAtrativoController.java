package com.GuapiVerde.mvp.controller;

import com.GuapiVerde.mvp.dto.CategoriaAtrativoEntrada;
import com.GuapiVerde.mvp.dto.CategoriaAtrativoResposta;
import com.GuapiVerde.mvp.service.CategoriaAtrativoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping("/api/categorias-atrativos")
@RequiredArgsConstructor
public class CategoriaAtrativoController {

    private final CategoriaAtrativoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaAtrativoResposta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaAtrativoResposta obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaAtrativoResposta cadastrar(
            @Valid @RequestBody CategoriaAtrativoEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaAtrativoResposta atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaAtrativoEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping({ "/{id}" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }

}