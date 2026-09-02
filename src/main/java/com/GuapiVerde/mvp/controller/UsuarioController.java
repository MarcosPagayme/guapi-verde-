package com.GuapiVerde.mvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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

@Tag(name = "Autenticação", description = "Cadastro, login e dados do usuário autenticado.")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    @Operation(summary = "Cadastrar registro", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recurso criado"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "409", ref = "#/components/responses/Erro409")
    })

     @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public CadastroUsuarioResponse cadastrar(
            @Valid @RequestBody CadastroUsuarioEntrada entrada
    ) {
        return usuarioService.cadastrarVisitante(entrada);
    }

    
}
