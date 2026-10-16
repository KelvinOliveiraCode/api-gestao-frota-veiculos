package com.kelvinoliveira.gestaofrota.dto;

import com.kelvinoliveira.gestaofrota.model.StatusManutencao;
import com.kelvinoliveira.gestaofrota.model.TipoManutencao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de saída de manutenção.
 */
public record ManutencaoResponse(
        Long id,
        Long veiculoId,
        String veiculoPlaca,
        TipoManutencao tipo,
        String descricao,
        LocalDate data,
        BigDecimal custo,
        Integer km,
        String oficina,
        StatusManutencao status,
        LocalDate dataConclusao,
        LocalDateTime dataCadastro) {
}
