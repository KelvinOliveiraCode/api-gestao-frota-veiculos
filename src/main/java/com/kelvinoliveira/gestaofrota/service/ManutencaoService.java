package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.ManutencaoRequest;
import com.kelvinoliveira.gestaofrota.dto.ManutencaoResponse;
import com.kelvinoliveira.gestaofrota.exception.ManutencaoNaoEncontradaException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.Manutencao;
import com.kelvinoliveira.gestaofrota.model.StatusManutencao;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.ManutencaoRepository;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso de ordens de manutenção.
 */
@Service
@Transactional
public class ManutencaoService {

    private final ManutencaoRepository repository;
    private final VeiculoRepository veiculoRepository;

    public ManutencaoService(ManutencaoRepository repository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorVeiculo(Long veiculoId) {
        if (!veiculoRepository.existsById(veiculoId)) {
            throw new VeiculoNaoEncontradoException(veiculoId);
        }
        return repository.findByVeiculoId(veiculoId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ManutencaoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new ManutencaoNaoEncontradaException(id)));
    }

    public ManutencaoResponse cadastrar(ManutencaoRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new VeiculoNaoEncontradoException(request.veiculoId()));
        Manutencao manutencao = new Manutencao(veiculo, request.tipo(), request.descricao(),
                request.data(), request.custo(), request.km(), request.oficina(),
                request.status() != null ? request.status() : StatusManutencao.AGENDADA);
        return toResponse(repository.save(manutencao));
    }

    public ManutencaoResponse concluir(Long id) {
        Manutencao manutencao = buscarEntidade(id);
        manutencao.setStatus(StatusManutencao.CONCLUIDA);
        manutencao.setDataConclusao(LocalDate.now());
        return toResponse(repository.save(manutencao));
    }

    public ManutencaoResponse cancelar(Long id) {
        Manutencao manutencao = buscarEntidade(id);
        manutencao.setStatus(StatusManutencao.CANCELADA);
        return toResponse(repository.save(manutencao));
    }

    public void remover(Long id) {
        buscarEntidade(id);
        repository.deleteById(id);
    }

    private Manutencao buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ManutencaoNaoEncontradaException(id));
    }

    private ManutencaoResponse toResponse(Manutencao m) {
        return new ManutencaoResponse(m.getId(), m.getVeiculo().getId(), m.getVeiculo().getPlaca(),
                m.getTipo(), m.getDescricao(), m.getData(), m.getCusto(), m.getKm(),
                m.getOficina(), m.getStatus(), m.getDataConclusao(), m.getDataCadastro());
    }
}
