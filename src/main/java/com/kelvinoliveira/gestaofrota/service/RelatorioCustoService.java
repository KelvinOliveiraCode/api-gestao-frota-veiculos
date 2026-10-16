package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoItem;
import com.kelvinoliveira.gestaofrota.dto.RelatorioCustoResponse;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.Custo;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.CustoRepository;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Relatório de custos de um veículo em um período, agrupado por tipo de custo.
 */
@Service
@Transactional(readOnly = true)
public class RelatorioCustoService {

    private final CustoRepository custoRepository;
    private final VeiculoRepository veiculoRepository;

    public RelatorioCustoService(CustoRepository custoRepository, VeiculoRepository veiculoRepository) {
        this.custoRepository = custoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public RelatorioCustoResponse gerar(Long veiculoId, LocalDate dataInicio, LocalDate dataFim) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(veiculoId));
        List<Custo> custos = custoRepository.findByVeiculoIdAndDataBetween(veiculoId, dataInicio, dataFim);

        BigDecimal valorTotal = custos.stream()
                .map(Custo::getValor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<TipoCusto, BigDecimal> totaisPorTipo = custos.stream()
                .collect(Collectors.groupingBy(Custo::getTipo,
                        Collectors.reducing(BigDecimal.ZERO, Custo::getValor, BigDecimal::add)));
        Map<TipoCusto, Long> quantidadePorTipo = custos.stream()
                .collect(Collectors.groupingBy(Custo::getTipo, Collectors.counting()));

        List<RelatorioCustoItem> itens = totaisPorTipo.entrySet().stream()
                .map(entry -> new RelatorioCustoItem(entry.getKey(), entry.getValue(),
                        quantidadePorTipo.get(entry.getKey()).intValue()))
                .sorted(Comparator.comparing(item -> item.tipo().name()))
                .toList();

        return new RelatorioCustoResponse(veiculoId, veiculo.getPlaca(), dataInicio, dataFim,
                valorTotal, itens);
    }
}
