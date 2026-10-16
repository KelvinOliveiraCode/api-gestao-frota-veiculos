package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoResponse;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.CustoRepository;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Relatório de custos de um veículo em um período (repositório real em H2 em memória).
 */
@DataJpaTest
@Import(RelatorioCustoService.class)
class RelatorioCustoServiceTest {

    @Autowired
    RelatorioCustoService service;

    @Autowired
    VeiculoRepository veiculoRepository;

    @Autowired
    CustoRepository custoRepository;

    /**
     * Insere um veículo com 4 custos em períodos distintos e retorna o ID do veículo.
     */
    private Long seed() {
        Veiculo veiculo = veiculoRepository.save(new Veiculo("ABC-1234", "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO));
        Long veiculoId = veiculo.getId();
        custoRepository.save(new com.kelvinoliveira.gestaofrota.model.Custo(veiculo, TipoCusto.COMBUSTIVEL, "Gasolina", new BigDecimal("100.00"), LocalDate.of(2024, 1, 10)));
        custoRepository.save(new com.kelvinoliveira.gestaofrota.model.Custo(veiculo, TipoCusto.COMBUSTIVEL, "Álcool", new BigDecimal("200.00"), LocalDate.of(2024, 1, 20)));
        custoRepository.save(new com.kelvinoliveira.gestaofrota.model.Custo(veiculo, TipoCusto.SEGURO, "Seguro anual", new BigDecimal("500.00"), LocalDate.of(2024, 1, 15)));
        custoRepository.save(new com.kelvinoliveira.gestaofrota.model.Custo(veiculo, TipoCusto.IPVA, "IPVA 2024", new BigDecimal("800.00"), LocalDate.of(2024, 2, 5)));
        return veiculoId;
    }

    @Test
    void should_gerar_relatorio_de_custos_por_veiculo_e_periodo() {
        Long veiculoId = seed();

        RelatorioCustoResponse relatorio = service.gerar(veiculoId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        Assertions.assertThat(relatorio.veiculoId()).isEqualTo(veiculoId);
        Assertions.assertThat(relatorio.veiculoPlaca()).isEqualTo("ABC-1234");
        Assertions.assertThat(relatorio.dataInicio()).isEqualTo(LocalDate.of(2024, 1, 1));
        Assertions.assertThat(relatorio.dataFim()).isEqualTo(LocalDate.of(2024, 1, 31));
        Assertions.assertThat(relatorio.valorTotal()).isEqualByComparingTo(new BigDecimal("800.00"));
        Assertions.assertThat(relatorio.itens()).hasSize(2);
        Assertions.assertThat(relatorio.itens().get(0).tipo()).isEqualTo(TipoCusto.COMBUSTIVEL);
        Assertions.assertThat(relatorio.itens().get(0).total()).isEqualByComparingTo(new BigDecimal("300.00"));
        Assertions.assertThat(relatorio.itens().get(0).quantidade()).isEqualTo(2);
        Assertions.assertThat(relatorio.itens().get(1).tipo()).isEqualTo(TipoCusto.SEGURO);
        Assertions.assertThat(relatorio.itens().get(1).total()).isEqualByComparingTo(new BigDecimal("500.00"));
        Assertions.assertThat(relatorio.itens().get(1).quantidade()).isEqualTo(1);
    }

    @Test
    void should_excluir_custos_fora_do_periodo() {
        Long veiculoId = seed();

        RelatorioCustoResponse relatorio = service.gerar(veiculoId, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));

        Assertions.assertThat(relatorio.valorTotal()).isEqualByComparingTo(new BigDecimal("800.00"));
        Assertions.assertThat(relatorio.itens()).hasSize(1);
        Assertions.assertThat(relatorio.itens().get(0).tipo()).isEqualTo(TipoCusto.IPVA);
    }

    @Test
    void should_retornar_total_zero_sem_custos_no_periodo() {
        Long veiculoId = seed();

        RelatorioCustoResponse relatorio = service.gerar(veiculoId, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        Assertions.assertThat(relatorio.valorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(relatorio.itens()).isEmpty();
    }

    @Test
    void should_lancar_excecao_quando_veiculo_inexistente() {
        Assertions.assertThatThrownBy(() -> service.gerar(99999L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }
}
