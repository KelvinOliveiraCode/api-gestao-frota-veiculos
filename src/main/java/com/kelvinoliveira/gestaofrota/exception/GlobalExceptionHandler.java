package com.kelvinoliveira.gestaofrota.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratador global de exceções: devolve corpos de erro padronizados com status HTTP adequado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroResposta> handleNegocio(NegocioException ex, HttpServletRequest request) {
        ErroResposta erro = new ErroResposta(LocalDateTime.now(),
                ex.getStatus().value(), ex.getStatus().getReasonPhrase(),
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErroResposta erro = new ErroResposta(LocalDateTime.now(), 400, "Erro de validação", mensagem,
                request.getRequestURI());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleInterno(Exception ex, HttpServletRequest request) {
        ErroResposta erro = new ErroResposta(LocalDateTime.now(), 500, "Erro interno do servidor",
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.internalServerError().body(erro);
    }
}
