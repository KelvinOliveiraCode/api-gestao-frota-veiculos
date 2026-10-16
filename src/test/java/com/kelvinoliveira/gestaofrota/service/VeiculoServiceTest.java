package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.VeiculoRequest;
import com.kelvinoliveira.gestaofrota.dto.VeiculoResponse;
import com.kelvinoliveira.gestaofrota.exception.PlacaDuplicadaException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso de veículos (repositório real em H2 em memória).
 */
@DataJpaTest
@Import(VeiculoService.class)
class VeiculoServiceTest {

    @Autowired
    VeiculoService service;

    @Autowired
    VeiculoRepository repository;

    private VeiculoRequest request(String placa) {
        return new VeiculoRequest(placa, "Toyota", "Corolla", 2020, "Prata", StatusVeiculo.ATIVO);
    }

    @Test
    void should_cadastrar_veiculo_quando_dados_validos() {
        VeiculoResponse resposta = service.cadastrar(request("ABC-1234"));

        Assertions.assertThat(resposta.id()).isNotNull();
        Assertions.assertThat(resposta.placa()).isEqualTo("ABC-1234");
        Assertions.assertThat(resposta.marca()).isEqualTo("Toyota");
        Assertions.assertThat(resposta.status()).isEqualTo(StatusVeiculo.ATIVO);
        Assertions.assertThat(resposta.dataCadastro()).isNotNull();
        Assertions.assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void should_nao_cadastrar_placa_duplicada_ignorando_caixa() {
        service.cadastrar(request("ABC-1234"));

        Assertions.assertThatThrownBy(() -> service.cadastrar(request("abc-1234")))
                .isInstanceOf(PlacaDuplicadaException.class);

        Assertions.assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @Transactional
    void should_buscar_veiculo_por_id() {
        VeiculoResponse criada = service.cadastrar(request("ABC-1234"));

        VeiculoResponse encontrada = service.buscarPorId(criada.id());

        Assertions.assertThat(encontrada.placa()).isEqualTo("ABC-1234");
        Assertions.assertThat(encontrada.marca()).isEqualTo("Toyota");
    }

    @Test
    void should_lancar_excecao_quando_veiculo_inexistente() {
        Assertions.assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }

    @Test
    void should_listar_veiculos_por_status() {
        VeiculoResponse primeira = service.cadastrar(request("ABC-1111"));
        service.cadastrar(request("ABC-2222"));
        repository.findById(primeira.id()).ifPresent(v -> v.setStatus(StatusVeiculo.EM_MANUTENCAO));
        repository.flush();

        List<VeiculoResponse> manutencao = service.listarPorStatus(StatusVeiculo.EM_MANUTENCAO);
        List<VeiculoResponse> ativos = service.listarPorStatus(StatusVeiculo.ATIVO);

        Assertions.assertThat(manutencao).hasSize(1);
        Assertions.assertThat(manutencao.get(0).placa()).isEqualTo("ABC-1111");
        Assertions.assertThat(ativos).hasSize(1);
        Assertions.assertThat(ativos.get(0).placa()).isEqualTo("ABC-2222");
    }

    @Test
    void should_atualizar_veiculo_existente() {
        VeiculoResponse criada = service.cadastrar(request("ABC-1234"));

        VeiculoResponse atualizada = service.atualizar(criada.id(),
                new VeiculoRequest("XYZ-9999", "Honda", "Civic", 2022, "Preto", StatusVeiculo.ATIVO));

        Assertions.assertThat(atualizada.placa()).isEqualTo("XYZ-9999");
        Assertions.assertThat(atualizada.marca()).isEqualTo("Honda");
        Assertions.assertThat(atualizada.ano()).isEqualTo(2022);
    }

    @Test
    void should_nao_atualizar_para_placa_ja_existente() {
        service.cadastrar(request("ABC-1111"));
        VeiculoResponse alvo = service.cadastrar(request("ABC-2222"));

        Assertions.assertThatThrownBy(() -> service.atualizar(alvo.id(),
                new VeiculoRequest("abc-1111", "Honda", "Civic", 2022, null, StatusVeiculo.ATIVO)))
                .isInstanceOf(PlacaDuplicadaException.class);
    }

    @Test
    void should_remover_veiculo_por_id() {
        VeiculoResponse criada = service.cadastrar(request("ABC-1234"));

        service.remover(criada.id());

        Assertions.assertThat(repository.count()).isZero();
        Assertions.assertThatThrownBy(() -> service.buscarPorId(criada.id()))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }
}
