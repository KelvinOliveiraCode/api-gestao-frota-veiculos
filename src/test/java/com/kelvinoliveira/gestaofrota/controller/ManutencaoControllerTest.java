package com.kelvinoliveira.gestaofrota.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelvinoliveira.gestaofrota.dto.ManutencaoRequest;
import com.kelvinoliveira.gestaofrota.dto.ManutencaoResponse;
import com.kelvinoliveira.gestaofrota.exception.ManutencaoNaoEncontradaException;
import com.kelvinoliveira.gestaofrota.model.StatusManutencao;
import com.kelvinoliveira.gestaofrota.model.TipoManutencao;
import com.kelvinoliveira.gestaofrota.exception.GlobalExceptionHandler;
import com.kelvinoliveira.gestaofrota.service.ManutencaoService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Camada REST de manutenções (serviço mockado).
 */
@WebMvcTest(ManutencaoController.class)
@Import(GlobalExceptionHandler.class)
class ManutencaoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ManutencaoService service;

    @Test
    void should_listar_manutencoes() throws Exception {
        ManutencaoResponse resp = new ManutencaoResponse(1L, 10L, "ABC-1234", TipoManutencao.PREVENTIVA, "Troca de óleo",
                LocalDate.of(2024, 1, 10), new BigDecimal("350.00"), 45000, "Oficina Central", StatusManutencao.AGENDADA, null, null);
        given(service.listar()).willReturn(List.of(resp));

        mockMvc.perform(get("/manutencoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].veiculoPlaca").value("ABC-1234"));
    }

    @Test
    void should_cadastrar_manutencao() throws Exception {
        ManutencaoRequest req = new ManutencaoRequest(10L, TipoManutencao.PREVENTIVA, "Revisão",
                LocalDate.of(2024, 2, 1), new BigDecimal("400.00"), 50000, "Oficina X", StatusManutencao.AGENDADA);
        ManutencaoResponse resp = new ManutencaoResponse(1L, 10L, "ABC-1234", TipoManutencao.PREVENTIVA, "Revisão",
                LocalDate.of(2024, 2, 1), new BigDecimal("400.00"), 50000, "Oficina X", StatusManutencao.AGENDADA, null, null);
        given(service.cadastrar(any(ManutencaoRequest.class))).willReturn(resp);

        mockMvc.perform(post("/manutencoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("PREVENTIVA"));
    }

    @Test
    void should_concluir_manutencao() throws Exception {
        ManutencaoResponse resp = new ManutencaoResponse(1L, 10L, "ABC-1234", TipoManutencao.PREVENTIVA, "Troca de óleo",
                LocalDate.of(2024, 1, 10), new BigDecimal("350.00"), 45000, "Oficina Central", StatusManutencao.CONCLUIDA, LocalDate.of(2024, 1, 15), null);
        given(service.concluir(anyLong())).willReturn(resp);

        mockMvc.perform(patch("/manutencoes/1/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void should_retornar_404_quando_manutencao_nao_existe() throws Exception {
        given(service.buscarPorId(99L)).willThrow(new ManutencaoNaoEncontradaException(99L));

        mockMvc.perform(get("/manutencoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_retornar_204_ao_remover() throws Exception {
        mockMvc.perform(delete("/manutencoes/5"))
                .andExpect(status().isNoContent());
    }
}
