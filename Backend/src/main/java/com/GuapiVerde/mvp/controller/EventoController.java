package com.GuapiVerde.mvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.GuapiVerde.mvp.dto.EventoEntrada;
import com.GuapiVerde.mvp.dto.EventoResponse;
import com.GuapiVerde.mvp.service.EventoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Eventos", description = "Consulta e administração de eventos.")
@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService service;
    @Operation(summary = "Listar publicados", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPublicados() {
        return service.listarPublicados();
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
    public List<EventoResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }
    @Operation(summary = "Listar registros por atrativo", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping("/atrativo/{atrativoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorAtrativo(@PathVariable Long atrativoId) {
        return service.listarPorAtrativo(atrativoId);
    }
    @Operation(summary = "Listar eventos por temporada", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping("/temporada/{temporadaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorTemporada(@PathVariable Long temporadaId) {
        return service.listarPorTemporada(temporadaId);
    }
    @Operation(summary = "Listar eventos por período", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {
        return service.listarPorPeriodo(inicio, fim);
    }
    @Operation(summary = "Consultar publicação por ID", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404")
    })

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse obterPublicadoPorId(@PathVariable Long id) {
        return service.obterPublicadoPorId(id);
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
    public EventoResponse cadastrar(@Valid @RequestBody EventoEntrada entrada) {
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
    public EventoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoEntrada entrada) {
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
    public EventoResponse ativar(@PathVariable Long id) {
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
