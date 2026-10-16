package com.kelvinoliveira.gestaofrota.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando se tenta cadastrar ou atualizar um veículo com placa já existente.
 */
public class PlacaDuplicadaException extends NegocioException {

    public PlacaDuplicadaException(String placa) {
        super("Já existe um veículo cadastrado com a placa: " + placa, HttpStatus.CONFLICT);
    }
}
