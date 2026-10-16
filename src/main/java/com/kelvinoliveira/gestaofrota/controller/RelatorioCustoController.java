package com.kelvinoliveira.gestaofrota.controller;

import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoResponse;
import com.kelvinoliveira.gestaofrota.service.RelatorioCustoService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST do relatório de custos por veículo e período.
 */
@RestController
@RequestMapping("/relatorios/custos")
public class RelatorioCustoController {

    private final RelatorioCustoService service;

    public RelatorioCustoController(RelatorioCustoService service) {
        this.service = service;
    }

    @GetMapping
    public RelatorioCustoResponse gerar(@RequestParam Long veiculoId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return service.gerar(veiculoId, dataInicio, dataFim);
    }
}
