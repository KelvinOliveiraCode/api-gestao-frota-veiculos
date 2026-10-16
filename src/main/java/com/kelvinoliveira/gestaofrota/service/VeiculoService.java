package com.kelvinoliveira.gestaofrota.service;

import com.kelvinoliveira.gestaofrota.dto.VeiculoRequest;
import com.kelvinoliveira.gestaofrota.dto.VeiculoResponse;
import com.kelvinoliveira.gestaofrota.exception.PlacaDuplicadaException;
import com.kelvinoliveira.gestaofrota.exception.VeiculoNaoEncontradoException;
import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import com.kelvinoliveira.gestaofrota.repository.VeiculoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso de veículos: cadastro, consulta, atualização e remoção.
 */
@Service
@Transactional
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> listarPorStatus(StatusVeiculo status) {
        return repository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeiculoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public VeiculoResponse cadastrar(VeiculoRequest request) {
        if (repository.existsByPlacaIgnoreCase(request.placa())) {
            throw new PlacaDuplicadaException(request.placa());
        }
        Veiculo veiculo = new Veiculo(request.placa(), request.marca(), request.modelo(),
                request.ano(), request.cor(),
                request.status() != null ? request.status() : StatusVeiculo.ATIVO);
        return toResponse(repository.save(veiculo));
    }

    public VeiculoResponse atualizar(Long id, VeiculoRequest request) {
        Veiculo veiculo = buscarEntidade(id);
        String placaNova = request.placa();
        if (!veiculo.getPlaca().equalsIgnoreCase(placaNova)
                && repository.existsByPlacaIgnoreCase(placaNova)) {
            throw new PlacaDuplicadaException(placaNova);
        }
        veiculo.setPlaca(placaNova);
        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setAno(request.ano());
        veiculo.setCor(request.cor());
        if (request.status() != null) {
            veiculo.setStatus(request.status());
        }
        return toResponse(repository.save(veiculo));
    }

    public void remover(Long id) {
        buscarEntidade(id);
        repository.deleteById(id);
    }

    private Veiculo buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(id));
    }

    private VeiculoResponse toResponse(Veiculo v) {
        return new VeiculoResponse(v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(),
                v.getAno(), v.getCor(), v.getStatus(), v.getDataCadastro());
    }
}
