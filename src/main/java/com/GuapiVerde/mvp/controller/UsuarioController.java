package com.GuapiVerde.mvp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.CadastroUsuarioEntrada;
import com.GuapiVerde.mvp.dto.CadastroUsuarioResponse;
import com.GuapiVerde.mvp.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

     @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public CadastroUsuarioResponse cadastrar(
            @Valid @RequestBody CadastroUsuarioEntrada entrada
    ) {
        return usuarioService.cadastrarVisitante(entrada);
    }

    
}
