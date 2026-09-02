package com.GuapiVerde.mvp.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    LocalDateTime dataHora,
    Integer status,
    String erro,
    String mensagem,
    String caminho,
    Map<String, String> campos
) {
}
