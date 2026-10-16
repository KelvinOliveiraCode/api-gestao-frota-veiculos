package com.kelvinoliveira.gestaofrota.dto;

import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import java.time.LocalDateTime;

/**
 * DTO de saída de veículo.
 */
public record VeiculoResponse(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        StatusVeiculo status,
        LocalDateTime dataCadastro) {
}
