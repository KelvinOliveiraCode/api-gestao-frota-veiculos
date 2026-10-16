package com.kelvinoliveira.gestaofrota.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando uma manutenção não existe para o ID informado.
 */
public class ManutencaoNaoEncontradaException extends NegocioException {

    public ManutencaoNaoEncontradaException(Long id) {
        super("Manutenção não encontrada: id=" + id, HttpStatus.NOT_FOUND);
    }
}
