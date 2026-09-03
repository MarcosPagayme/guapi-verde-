package com.GuapiVerde.mvp.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()));

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Um ou mais campos estão inválidos.",
                request.getRequestURI(),
                erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Requisição inválida",
                "O conteúdo da requisição está inválido. Verifique os valores informados.",
                request.getRequestURI(),
                null);

        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Recurso duplicado",
                ex.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Conflito de dados",
                "Este relacionamento já está cadastrado.",
                request.getRequestURI(),
                null);

        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorResponse> handleRegraDeNegocioException(
            RegraDeNegocioException ex,
            HttpServletRequest request) {
        ErrorResponse erro = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negócio violada",
                ex.getMessage(),
                request.getRequestURI(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AuthenticationException.class)
public ResponseEntity<ErrorResponse> tratarErroDeAutenticacao(
        AuthenticationException excecao,
        HttpServletRequest requisicao
) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    ErrorResponse erro = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            "Não autorizado",
            "E-mail ou senha inválidos.",
            requisicao.getRequestURI(),
            null
    );

    return ResponseEntity.status(status).body(erro);
}
}
