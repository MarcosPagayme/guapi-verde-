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

import com.GuapiVerde.mvp.dto.CupomEntrada;
import com.GuapiVerde.mvp.dto.CupomResponse;
import com.GuapiVerde.mvp.service.CupomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cupons")
@RequiredArgsConstructor
public class CupomController {

    private final CupomService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CupomResponse> listarDisponiveis() {
        return service.listarDisponiveis();
    }

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<CupomResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }

    @GetMapping("/administracao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterParaAdministracaoPorId(@PathVariable Long id) {
        return service.obterParaAdministracaoPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterDisponivelPorCodigo(@PathVariable String codigo) {
        return service.obterDisponivelPorCodigo(codigo);
    }

    @GetMapping("/campanha/{campanhaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CupomResponse> listarDisponiveisPorCampanha(
            @PathVariable Long campanhaId) {
        return service.listarDisponiveisPorCampanha(campanhaId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterDisponivelPorId(@PathVariable Long id) {
        return service.obterDisponivelPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CupomResponse cadastrar(@Valid @RequestBody CupomEntrada entrada) {
        return service.cadastrar(entrada);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CupomEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
