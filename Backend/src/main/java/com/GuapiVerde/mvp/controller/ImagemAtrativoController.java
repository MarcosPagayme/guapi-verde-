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

import com.GuapiVerde.mvp.dto.ImagemAtrativoEntrada;
import com.GuapiVerde.mvp.dto.ImagemAtrativoResponse;
import com.GuapiVerde.mvp.service.ImagemAtrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Imagens dos atrativos", description = "Consulta e administração das imagens dos atrativos.")
@RestController
@RequestMapping("/api/imagens-atrativos")
@RequiredArgsConstructor
public class ImagemAtrativoController {

    private final ImagemAtrativoService service;
    @Operation(summary = "Listar registros", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ImagemAtrativoResponse> listar() {
        return service.listar();
    }
    @Operation(summary = "Listar registros por atrativo", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída")
    })

    @GetMapping("/atrativo/{atrativoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ImagemAtrativoResponse> listarPorAtrativo(@PathVariable Long atrativoId) {
        return service.listarPorAtrativo(atrativoId);
    }
    @Operation(summary = "Consultar registro por ID", description = "Endpoint público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404")
    })

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImagemAtrativoResponse obterPorId(@PathVariable Long id) {
        return service.obterPorId(id);
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
    public ImagemAtrativoResponse cadastrar(
            @Valid @RequestBody ImagemAtrativoEntrada entrada) {
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
    public ImagemAtrativoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ImagemAtrativoEntrada entrada) {
        return service.atualizar(id, entrada);
    }
    @Operation(summary = "Excluir registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Operação concluída sem conteúdo"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
