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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.PreferenciaResponse;
import com.GuapiVerde.mvp.service.PreferenciaService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Preferências", description = "Categorias preferidas do usuário autenticado.")
@RestController
@RequestMapping("/api/preferencias")
@RequiredArgsConstructor
public class PreferenciaController {

    private final PreferenciaService service;
    @Operation(summary = "Listar registros", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PreferenciaResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return service.listarDoUsuario(jwt.getSubject());
    }
    @Operation(summary = "Adicionar registro", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recurso criado"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "409", ref = "#/components/responses/Erro409"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping("/{categoriaAtrativoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PreferenciaResponse adicionar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoriaAtrativoId) {
        return service.adicionar(jwt.getSubject(), categoriaAtrativoId);
    }
    @Operation(summary = "Remover registro", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Operação concluída sem conteúdo"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @DeleteMapping("/{categoriaAtrativoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoriaAtrativoId) {
        service.remover(jwt.getSubject(), categoriaAtrativoId);
    }
}
