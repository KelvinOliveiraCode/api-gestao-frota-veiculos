package com.kelvinoliveira.gestaofrota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;

/**
 * DTO de entrada para cadastro/atualização de custo.
 */
public record CustoRequest(
        @NotNull(message = "O veículo é obrigatório.") Long veiculoId,
        TipoCusto tipo,
        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 200) String descricao,
        @NotNull(message = "O valor é obrigatório.") BigDecimal valor,
        @NotNull(message = "A data é obrigatória.") LocalDate data) {
}
