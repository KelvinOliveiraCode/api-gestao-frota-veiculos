package com.kelvinoliveira.gestaofrota.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelvinoliveira.gestaofrota.dto.VeiculoRequest;
import com.kelvinoliveira.gestaofrota.dto.VeiculoResponse;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.exception.GlobalExceptionHandler;
import com.kelvinoliveira.gestaofrota.service.VeiculoService;
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
 * Camada REST de veículos (serviço mockado).
 */
@WebMvcTest(VeiculoController.class)
@Import(GlobalExceptionHandler.class)
class VeiculoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    VeiculoService service;

    @Test
    void should_listar_veiculos() throws Exception {
        VeiculoResponse resp = new VeiculoResponse(1L, "ABC-1234", "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO, null);
        given(service.listar()).willReturn(List.of(resp));

        mockMvc.perform(get("/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].placa").value("ABC-1234"));
    }

    @Test
    void should_cadastrar_veiculo() throws Exception {
        VeiculoRequest req = new VeiculoRequest("ABC-1234", "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO);
        VeiculoResponse resp = new VeiculoResponse(1L, "ABC-1234", "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO, null);
        given(service.cadastrar(any(VeiculoRequest.class))).willReturn(resp);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value("ABC-1234"));
    }

    @Test
    void should_retornar_400_quando_placa_em_branco() throws Exception {
        VeiculoRequest req = new VeiculoRequest("", "Toyota", "Corolla", null, null, StatusVeiculo.ATIVO);

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_retornar_404_quando_veiculo_nao_existente() throws Exception {
        given(service.buscarPorId(99L)).willThrow(new VeiculoNaoEncontradoException(99L));

        mockMvc.perform(get("/veiculos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_retornar_204_ao_remover() throws Exception {
        mockMvc.perform(delete("/veiculos/5"))
                .andExpect(status().isNoContent());
    }
}
