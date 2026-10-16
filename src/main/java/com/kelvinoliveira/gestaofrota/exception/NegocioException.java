package com.kelvinoliveira.gestaofrota.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção de negócio que carrega o status HTTP a ser devolvido ao cliente.
 */
public abstract class NegocioException extends RuntimeException {

    private final HttpStatus status;

    protected NegocioException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
