package com.kelvinoliveira.gestaofrota.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoItem;
import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoResponse;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.exception.GlobalExceptionHandler;
import com.kelvinoliveira.gestaofrota.service.RelatorioCustoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDate.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Camada REST do relatório de custos (serviço mockado).
 */
@WebMvcTest(RelatorioCustoController.class)
@Import(GlobalExceptionHandler.class)
class RelatorioCustoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RelatorioCustoService service;

    @Test
    void should_gerar_relatorio_de_custos() throws Exception {
        RelatorioCustoResponse relatorio = new RelatorioCustoResponse(1L, "ABC-1234", of(2024, 1, 1), of(2024, 1, 31),
                new BigDecimal("800.00"), List.of(new RelatorioCustoItem(TipoCusto.COMBUSTIVEL, new BigDecimal("800.00"), 2)));
        given(service.gerar(anyLong(), any(LocalDate.class), any(LocalDate.class))).willReturn(relatorio);

        mockMvc.perform(get("/relatorios/custos")
                        .param("veiculoId", "1")
                        .param("dataInicio", "2024-01-01")
                        .param("dataFim", "2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculoPlaca").value("ABC-1234"))
                .andExpect(jsonPath("$.valorTotal").value(800.00))
                .andExpect(jsonPath("$.itens[0].tipo").value("COMBUSTIVEL"))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2));
    }
}
