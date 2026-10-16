package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.ManutencaoRequest;
import com.kelvinoliveira.gestaofrota.dto.ManutencaoResponse;
import com.kelvinoliveira.gestaofrota.exception.ManutencaoNaoEncontradaException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.Manutencao;
import com.kelvinoliveira.gestaofrota.model.StatusManutencao;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.model.TipoManutencao;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.ManutencaoRepository;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Casos de uso de manutenções (repositório real em H2 em memória).
 */
@DataJpaTest
@Import(ManutencaoService.class)
class ManutencaoServiceTest {

    @Autowired
    ManutencaoService service;

    @Autowired
    VeiculoRepository veiculoRepository;

    @Autowired
    ManutencaoRepository manutencaoRepository;

    private Veiculo veiculo(String placa) {
        return veiculoRepository.save(new Veiculo(placa, "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO));
    }

    private ManutencaoRequest request(Long veiculoId) {
        return new ManutencaoRequest(veiculoId, TipoManutencao.PREVENTIVA, "Troca de óleo",
                LocalDate.of(2024, 1, 10), new BigDecimal("350.00"), 45000, "Oficina Central", StatusManutencao.AGENDADA);
    }

    @Test
    void should_cadastrar_manutencao_para_veiculo_existente() {
        Veiculo veiculo = veiculo("ABC-1234");

        ManutencaoResponse resposta = service.cadastrar(request(veiculo.getId()));

        Assertions.assertThat(resposta.id()).isNotNull();
        Assertions.assertThat(resposta.veiculoId()).isEqualTo(veiculo.getId());
        Assertions.assertThat(resposta.veiculoPlaca()).isEqualTo("ABC-1234");
        Assertions.assertThat(resposta.tipo()).isEqualTo(TipoManutencao.PREVENTIVA);
        Assertions.assertThat(resposta.status()).isEqualTo(StatusManutencao.AGENDADA);
        Assertions.assertThat(manutencaoRepository.count()).isEqualTo(1);
    }

    @Test
    void should_lancar_excecao_quando_veiculo_inexistente() {
        Assertions.assertThatThrownBy(() -> service.cadastrar(request(999L)))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }

    @Test
    void should_listar_manutencoes_por_veiculo() {
        Veiculo v1 = veiculo("ABC-1111");
        Veiculo v2 = veiculo("ABC-2222");
        service.cadastrar(request(v1.getId()));
        service.cadastrar(request(v1.getId()));
        service.cadastrar(request(v2.getId()));

        List<ManutencaoResponse> doVeiculo1 = service.listarPorVeiculo(v1.getId());

        Assertions.assertThat(doVeiculo1).hasSize(2);
    }

    @Test
    void should_lancar_excecao_quando_listar_por_veiculo_inexistente() {
        Assertions.assertThatThrownBy(() -> service.listarPorVeiculo(999L))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }

    @Test
    void should_concluir_manutencao_agendada() {
        Veiculo veiculo = veiculo("ABC-1234");
        ManutencaoResponse criada = service.cadastrar(request(veiculo.getId()));

        ManutencaoResponse concluida = service.concluir(criada.id());

        Assertions.assertThat(concluida.status()).isEqualTo(StatusManutencao.CONCLUIDA);
        Assertions.assertThat(concluida.dataConclusao()).isNotNull();
    }

    @Test
    void should_cancelar_manutencao() {
        Veiculo veiculo = veiculo("ABC-1234");
        ManutencaoResponse criada = service.cadastrar(request(veiculo.getId()));

        ManutencaoResponse cancelada = service.cancelar(criada.id());

        Assertions.assertThat(cancelada.status()).isEqualTo(StatusManutencao.CANCELADA);
    }

    @Test
    void should_lancar_excecao_quando_manutencao_inexistente() {
        Assertions.assertThatThrownBy(() -> service.concluir(999L))
                .isInstanceOf(ManutencaoNaoEncontradaException.class);
    }

    @Test
    void should_remover_manutencao_por_id() {
        Veiculo veiculo = veiculo("ABC-1234");
        ManutencaoResponse criada = service.cadastrar(request(veiculo.getId()));

        service.remover(criada.id());

        Assertions.assertThat(manutencaoRepository.count()).isZero();
    }
}
