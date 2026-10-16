package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.CustoRequest;
import com.kelvinoliveira.gestaofrota.dto.CustoResponse;
import com.kelvinoliveira.gestaofrota.exception.CustoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.CustoRepository;
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
 * Casos de uso de custos (repositório real em H2 em memória).
 */
@DataJpaTest
@Import(CustoService.class)
class CustoServiceTest {

    @Autowired
    CustoService service;

    @Autowired
    VeiculoRepository veiculoRepository;

    @Autowired
    CustoRepository custoRepository;

    private Veiculo veiculo(String placa) {
        return veiculoRepository.save(new Veiculo(placa, "Ford", "Ka", 2019, "Branco", StatusVeiculo.ATIVO));
    }

    private CustoRequest request(Long veiculoId) {
        return new CustoRequest(veiculoId, TipoCusto.COMBUSTIVEL, "Abastecimento",
                new BigDecimal("250.00"), LocalDate.of(2024, 2, 5));
    }

    @Test
    void should_cadastrar_custo_para_veiculo_existente() {
        Veiculo veiculo = veiculo("ABC-1234");

        CustoResponse resposta = service.cadastrar(request(veiculo.getId()));

        Assertions.assertThat(resposta.id()).isNotNull();
        Assertions.assertThat(resposta.veiculoId()).isEqualTo(veiculo.getId());
        Assertions.assertThat(resposta.tipo()).isEqualTo(TipoCusto.COMBUSTIVEL);
        Assertions.assertThat(resposta.valor()).isEqualByComparingTo(new BigDecimal("250.00"));
        Assertions.assertThat(custoRepository.count()).isEqualTo(1);
    }

    @Test
    void should_lancar_excecao_quando_veiculo_inexistente() {
        Assertions.assertThatThrownBy(() -> service.cadastrar(request(999L)))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }

    @Test
    void should_listar_custos_por_veiculo() {
        Veiculo v1 = veiculo("ABC-1111");
        Veiculo v2 = veiculo("ABC-2222");
        service.cadastrar(request(v1.getId()));
        service.cadastrar(request(v1.getId()));
        service.cadastrar(request(v2.getId()));

        List<CustoResponse> doVeiculo1 = service.listarPorVeiculo(v1.getId());

        Assertions.assertThat(doVeiculo1).hasSize(2);
    }

    @Test
    void should_calcular_valor_total_por_veiculo() {
        Veiculo veiculo = veiculo("ABC-1234");
        service.cadastrar(new CustoRequest(veiculo.getId(), TipoCusto.COMBUSTIVEL, "Gasolina",
                new BigDecimal("150.00"), LocalDate.of(2024, 1, 10)));
        service.cadastrar(new CustoRequest(veiculo.getId(), TipoCusto.SEGURO, "Seguro anual",
                new BigDecimal("1200.00"), LocalDate.of(2024, 3, 1)));

        BigDecimal total = service.valorTotalPorVeiculo(veiculo.getId());

        Assertions.assertThat(total).isEqualByComparingTo(new BigDecimal("1350.00"));
    }

    @Test
    void should_remover_custo_por_id() {
        Veiculo veiculo = veiculo("ABC-1234");
        CustoResponse criada = service.cadastrar(request(veiculo.getId()));

        service.remover(criada.id());

        Assertions.assertThat(custoRepository.count()).isZero();
    }
}
