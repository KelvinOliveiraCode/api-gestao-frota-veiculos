package com.kelvinoliveira.gestaofrota.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Resposta do relatório de custos de um veículo em um período.
 */
public record RelatorioCustoResponse(
        Long veiculoId,
        String veiculoPlaca,
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal valorTotal,
        List<RelatorioCustoItem> itens) {
}
