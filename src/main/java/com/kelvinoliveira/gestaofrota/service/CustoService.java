package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.CustoRequest;
import com.kelvinoliveira.gestaofrota.dto.CustoResponse;
import com.kelvinoliveira.gestaofrota.exception.CustoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.Custo;
import com.kelvinoliveira.gestaofrota.model.TipoCusto;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.CustoRepository;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso de custos da frota.
 */
@Service
@Transactional
public class CustoService {

    private final CustoRepository repository;
    private final VeiculoRepository veiculoRepository;

    public CustoService(CustoRepository repository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<CustoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustoResponse> listarPorVeiculo(Long veiculoId) {
        if (!veiculoRepository.existsById(veiculoId)) {
            throw new VeiculoNaoEncontradoException(veiculoId);
        }
        return repository.findByVeiculoId(veiculoId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new CustoNaoEncontradoException(id)));
    }

    public CustoResponse cadastrar(CustoRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new VeiculoNaoEncontradoException(request.veiculoId()));
        Custo custo = new Custo(veiculo, request.tipo(), request.descricao(),
                request.valor(), request.data());
        return toResponse(repository.save(custo));
    }

    public void remover(Long id) {
        buscarEntidade(id);
        repository.deleteById(id);
    }

    private Custo buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustoNaoEncontradoException(id));
    }

    private CustoResponse toResponse(Custo c) {
        return new CustoResponse(c.getId(), c.getVeiculo().getId(), c.getVeiculo().getPlaca(),
                c.getTipo(), c.getDescricao(), c.getValor(), c.getData(), c.getDataCadastro());
    }

    public BigDecimal valorTotalPorVeiculo(Long veiculoId) {
        return repository.findByVeiculoId(veiculoId).stream()
                .map(Custo::getValor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
