package com.GuapiVerde.mvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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

import com.GuapiVerde.mvp.dto.TemporadaEntrada;
import com.GuapiVerde.mvp.dto.TemporadaResponse;
import com.GuapiVerde.mvp.service.TemporadaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Temporadas", description = "Consulta e administração de temporadas.")
@RestController
@RequestMapping("/api/temporadas")
@RequiredArgsConstructor
public class TemporadaController {

    private final TemporadaService service;
    @Operation(summary = "Listar publicações", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TemporadaResponse> listarPublicadas() {
        return service.listarPublicadas();
    }
    @Operation(summary = "Listar registros para administração", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/administracao")
    @ResponseStatus(HttpStatus.OK)
    public List<TemporadaResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }
    @Operation(summary = "Consultar publicação por ID", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404")
    })

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse obterPublicadaPorId(@PathVariable Long id) {
        return service.obterPublicadaPorId(id);
    }
    @Operation(summary = "Cadastrar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recurso criado"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemporadaResponse cadastrar(
            @Valid @RequestBody TemporadaEntrada entrada) {
        return service.cadastrar(entrada);
    }
    @Operation(summary = "Atualizar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TemporadaEntrada entrada) {
        return service.atualizar(id, entrada);
    }
    @Operation(summary = "Ativar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public TemporadaResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }
    @Operation(summary = "Desativar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Operação concluída sem conteúdo"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}
