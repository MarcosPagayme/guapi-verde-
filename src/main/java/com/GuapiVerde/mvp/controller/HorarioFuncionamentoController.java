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

import com.GuapiVerde.mvp.dto.HorarioFuncionamentoEntrada;
import com.GuapiVerde.mvp.dto.HorarioFuncionamentoResponse;
import com.GuapiVerde.mvp.service.HorarioFuncionamentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/horarios-funcionamento")
@RequiredArgsConstructor
public class HorarioFuncionamentoController {

    private final HorarioFuncionamentoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HorarioFuncionamentoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/atrativo/{atrativoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<HorarioFuncionamentoResponse> listarPorAtrativo(@PathVariable Long atrativoId) {
        return service.listarPorAtrativo(atrativoId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HorarioFuncionamentoResponse obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HorarioFuncionamentoResponse cadastrar(
            @Valid @RequestBody HorarioFuncionamentoEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HorarioFuncionamentoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioFuncionamentoEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
