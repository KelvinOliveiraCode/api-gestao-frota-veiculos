package com.kelvinoliveira.gestaofrota.dto;

import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de saída de custo.
 */
public record CustoResponse(
        Long id,
        Long veiculoId,
        String veiculoPlaca,
        TipoCusto tipo,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        LocalDateTime dataCadastro) {
}
