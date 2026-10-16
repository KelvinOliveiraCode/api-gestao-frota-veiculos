package com.kelvinoliveira.gestaofrota.dto;

import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import java.math.BigDecimal;

/**
 * Item do relatório de custos, agrupado por tipo de custo.
 */
public record RelatorioCustoItem(TipoCusto tipo, BigDecimal total, int quantidade) {
}
