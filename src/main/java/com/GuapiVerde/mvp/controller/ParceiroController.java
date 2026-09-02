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

import com.GuapiVerde.mvp.dto.ParceiroEntrada;
import com.GuapiVerde.mvp.dto.ParceiroResponse;
import com.GuapiVerde.mvp.service.ParceiroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parceiros")
@RequiredArgsConstructor
public class ParceiroController {

    private final ParceiroService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParceiroResponse> listarPublicados() {
        return service.listarPublicados();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<ParceiroResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/administracao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ParceiroResponse obterParaAdministracaoPorId(@PathVariable Long id) {
        return service.obterParaAdministracaoPorId(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ParceiroResponse obterPublicadoPorId(@PathVariable Long id) {
        return service.obterPublicadoPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParceiroResponse cadastrar(
            @Valid @RequestBody ParceiroEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ParceiroResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ParceiroEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public ParceiroResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
