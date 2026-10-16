package com.kelvinoliveira.gestaofrota.controller;

import com.kelvinoliveira.gestaofrota.dto.ManutencaoRequest;
import com.kelvinoliveira.gestaofrota.dto.ManutencaoResponse;
import com.kelvinoliveira.gestaofrota.service.ManutencaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST de ordens de manutenção (CRUD + conclusão/cancelamento).
 */
@RestController
@RequestMapping("/manutencoes")
public class ManutencaoController {

    private final ManutencaoService service;

    public ManutencaoController(ManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ManutencaoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ManutencaoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<ManutencaoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return service.listarPorVeiculo(veiculoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManutencaoResponse cadastrar(@Valid @RequestBody ManutencaoRequest request) {
        return service.cadastrar(request);
    }

    @PatchMapping("/{id}/concluir")
    public ManutencaoResponse concluir(@PathVariable Long id) {
        return service.concluir(id);
    }

    @PatchMapping("/{id}/cancelar")
    public ManutencaoResponse cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
