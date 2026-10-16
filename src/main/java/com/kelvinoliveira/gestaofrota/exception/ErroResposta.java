package com.kelvinoliveira.gestaofrota.exception;

import java.time.LocalDateTime;

/**
 * Corpo padronizado de respostas de erro da API.
 */
public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho) {
}
