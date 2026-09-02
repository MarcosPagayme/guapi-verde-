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

import com.GuapiVerde.mvp.dto.CampanhaEntrada;
import com.GuapiVerde.mvp.dto.CampanhaResponse;
import com.GuapiVerde.mvp.service.CampanhaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/campanhas")
@RequiredArgsConstructor
public class CampanhaController {

    private final CampanhaService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CampanhaResponse> listarPublicadas() {
        return service.listarPublicadas();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<CampanhaResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/administracao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CampanhaResponse obterParaAdministracaoPorId(@PathVariable Long id) {
        return service.obterParaAdministracaoPorId(id);
    }

    @GetMapping("/parceiro/{parceiroId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CampanhaResponse> listarPublicadasPorParceiro(
            @PathVariable Long parceiroId) {
        return service.listarPublicadasPorParceiro(parceiroId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CampanhaResponse obterPublicadaPorId(@PathVariable Long id) {
        return service.obterPublicadaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampanhaResponse cadastrar(
            @Valid @RequestBody CampanhaEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CampanhaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CampanhaEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public CampanhaResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
