package com.gabrielyorlando.locacao.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(it -> fieldErrors.put(it.getField(), it.getDefaultMessage()));

        return buildErrorResponse("Erro de validação", fieldErrors, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, Object> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse("Recurso não encontrado", ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BusinessRuleException.class)
    public Map<String, Object> handleBusinessRuleException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse("Erro de regra de negócio", ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleGeneralError(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado na requisição: {}", request.getRequestURI(), ex);
        return buildErrorResponse("Erro interno no servidor", "Ocorreu um erro inesperado. Tente novamente mais tarde.", request.getRequestURI());
    }

    private Map<String, Object> buildErrorResponse(String error, Object message, String path) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        response.put("path", path);
        return response;
    }
}
