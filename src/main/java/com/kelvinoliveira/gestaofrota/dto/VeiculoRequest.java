package com.kelvinoliveira.gestaofrota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;

/**
 * DTO de entrada para cadastro/atualização de veículo.
 */
public record VeiculoRequest(
        @NotBlank(message = "A placa é obrigatória.")
        @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres.")
        String placa,
        @NotBlank(message = "A marca é obrigatória.")
        @Size(max = 60) String marca,
        @NotBlank(message = "O modelo é obrigatório.")
        @Size(max = 60) String modelo,
        Integer ano,
        @Size(max = 30) String cor,
        StatusVeiculo status) {
}
