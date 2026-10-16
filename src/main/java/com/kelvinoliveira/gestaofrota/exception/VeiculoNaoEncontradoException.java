package com.kelvinoliveira.gestaofrota.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando um veículo não existe para o ID informado.
 */
public class VeiculoNaoEncontradoException extends NegocioException {

    public VeiculoNaoEncontradoException(Long id) {
        super("Veículo não encontrado: id=" + id, HttpStatus.NOT_FOUND);
    }
}
