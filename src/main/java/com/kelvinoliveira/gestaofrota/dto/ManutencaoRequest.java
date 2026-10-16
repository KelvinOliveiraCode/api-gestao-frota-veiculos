package com.kelvinoliveira.gestaofrota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.kelvinoliveira.gestaofrota.model.StatusManutencao;
import com.kelvinoliveira.gestaofrota.model.TipoManutencao;

/**
 * DTO de entrada para cadastro/atualização de manutenção.
 */
public record ManutencaoRequest(
        @NotNull(message = "O veículo é obrigatório.") Long veiculoId,
        TipoManutencao tipo,
        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 200) String descricao,
        @NotNull(message = "A data é obrigatória.") LocalDate data,
        BigDecimal custo,
        Integer km,
        @Size(max = 100) String oficina,
        StatusManutencao status) {
}
