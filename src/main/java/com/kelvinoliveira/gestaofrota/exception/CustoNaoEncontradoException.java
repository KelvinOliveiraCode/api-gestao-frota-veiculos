package com.kelvinoliveira.gestaofrota.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando um custo não existe para o ID informado.
 */
public class CustoNaoEncontradoException extends NegocioException {

    public CustoNaoEncontradoException(Long id) {
        super("Custo não encontrado: id=" + id, HttpStatus.NOT_FOUND);
    }
}
