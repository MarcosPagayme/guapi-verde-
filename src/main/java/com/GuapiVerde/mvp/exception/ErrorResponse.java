package com.GuapiVerde.mvp.exception;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Resposta padronizada de erro da API")
public record ErrorResponse(
    @Schema(example = "2026-09-15T09:00:00") LocalDateTime dataHora,
    Integer status,
    String erro,
    String mensagem,
    String caminho,
    Map<String, String> campos
) {
}
