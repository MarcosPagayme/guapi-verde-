package com.GuapiVerde.mvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.ConsentimentoEntrada;
import com.GuapiVerde.mvp.dto.ConsentimentoResponse;
import com.GuapiVerde.mvp.service.ConsentimentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Consentimentos", description = "Histórico de decisões de consentimento do usuário.")
@RestController
@RequestMapping("/api/consentimentos")
@RequiredArgsConstructor
public class ConsentimentoController {

    private final ConsentimentoService service;
    @Operation(summary = "Listar histórico de consentimentos", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConsentimentoResponse> listarHistorico(@AuthenticationPrincipal Jwt jwt) {
        return service.listarHistoricoDoUsuario(jwt.getSubject());
    }
    @Operation(summary = "Listar consentimentos atuais", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/atuais")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsentimentoResponse> listarAtuais(@AuthenticationPrincipal Jwt jwt) {
        return service.listarAtuaisDoUsuario(jwt.getSubject());
    }
    @Operation(summary = "Registrar consentimento", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recurso criado"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "409", ref = "#/components/responses/Erro409"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsentimentoResponse registrar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ConsentimentoEntrada entrada) {
        return service.registrar(jwt.getSubject(), entrada);
    }
}
