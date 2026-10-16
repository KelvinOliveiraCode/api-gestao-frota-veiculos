package com.kelvinoliveira.gestaofrota.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelvinoliveira.gestaofrota.dto.CustoRequest;
import com.kelvinoliveira.gestaofrota.dto.CustoResponse;
import com.kelvinoliveira.gestaofrota.exception.CustoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.exception.GlobalExceptionHandler;
import com.kelvinoliveira.gestaofrota.service.CustoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Camada REST de custos (serviço mockado).
 */
@WebMvcTest(CustoController.class)
@Import(GlobalExceptionHandler.class)
class CustoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustoService service;

    @Test
    void should_listar_custos() throws Exception {
        CustoResponse resp = new CustoResponse(1L, 10L, "ABC-1234", TipoCusto.COMBUSTIVEL, "Abastecimento",
                new BigDecimal("250.00"), LocalDate.of(2024, 2, 5), null);
        given(service.listar()).willReturn(List.of(resp));

        mockMvc.perform(get("/custos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("COMBUSTIVEL"));
    }

    @Test
    void should_cadastrar_custo() throws Exception {
        CustoRequest req = new CustoRequest(10L, TipoCusto.SEGURO, "Seguro anual", new BigDecimal("1200.00"), LocalDate.of(2024, 3, 1));
        CustoResponse resp = new CustoResponse(1L, 10L, "ABC-1234", TipoCusto.SEGURO, "Seguro anual",
                new BigDecimal("1200.00"), LocalDate.of(2024, 3, 1), null);
        given(service.cadastrar(any(CustoRequest.class))).willReturn(resp);

        mockMvc.perform(post("/custos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(1200.00));
    }

    @Test
    void should_retornar_404_quando_custo_nao_existe() throws Exception {
        given(service.buscarPorId(99L)).willThrow(new CustoNaoEncontradoException(99L));

        mockMvc.perform(get("/custos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_retornar_204_ao_remover() throws Exception {
        mockMvc.perform(delete("/custos/5"))
                .andExpect(status().isNoContent());
    }
}
