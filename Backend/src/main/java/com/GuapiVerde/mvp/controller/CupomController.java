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

import com.GuapiVerde.mvp.dto.CupomEntrada;
import com.GuapiVerde.mvp.dto.CupomResponse;
import com.GuapiVerde.mvp.service.CupomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cupons", description = "Consulta e administração de cupons.")
@RestController
@RequestMapping("/api/cupons")
@RequiredArgsConstructor
public class CupomController {

    private final CupomService service;
    @Operation(summary = "Listar cupons disponíveis", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CupomResponse> listarDisponiveis() {
        return service.listarDisponiveis();
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
    public List<CupomResponse> listarParaAdministracao() {
        return service.listarParaAdministracao();
    }
    @Operation(summary = "Consultar registro para administração", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/administracao/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterParaAdministracaoPorId(@PathVariable Long id) {
        return service.obterParaAdministracaoPorId(id);
    }
    @Operation(summary = "Consultar cupom por código", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/codigo/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterDisponivelPorCodigo(@PathVariable String codigo) {
        return service.obterDisponivelPorCodigo(codigo);
    }
    @Operation(summary = "Listar cupons por campanha", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/campanha/{campanhaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CupomResponse> listarDisponiveisPorCampanha(
            @PathVariable Long campanhaId) {
        return service.listarDisponiveisPorCampanha(campanhaId);
    }
    @Operation(summary = "Consultar cupom por ID", description = "Requer autenticação JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401")
    })
    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse obterDisponivelPorId(@PathVariable Long id) {
        return service.obterDisponivelPorId(id);
    }
    @Operation(summary = "Cadastrar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recurso criado"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "409", ref = "#/components/responses/Erro409"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CupomResponse cadastrar(@Valid @RequestBody CupomEntrada entrada) {
        return service.cadastrar(entrada);
    }
    @Operation(summary = "Atualizar registro", description = "Requer autenticação JWT com perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação concluída"),
        @ApiResponse(responseCode = "400", ref = "#/components/responses/Erro400"),
        @ApiResponse(responseCode = "404", ref = "#/components/responses/Erro404"),
        @ApiResponse(responseCode = "409", ref = "#/components/responses/Erro409"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Erro401"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Erro403")
    })
    @SecurityRequirement(name = "bearerAuth")

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CupomResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CupomEntrada entrada) {
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
    public CupomResponse ativar(@PathVariable Long id) {
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
