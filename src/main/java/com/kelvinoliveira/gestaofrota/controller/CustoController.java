package com.kelvinoliveira.gestaofrota.controller;

import com.kelvinoliveira.gestaofrota.dto.CustoRequest;
import com.kelvinoliveira.gestaofrota.dto.CustoResponse;
import com.kelvinoliveira.gestaofrota.service.CustoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST de custos da frota (CRUD completo).
 */
@RestController
@RequestMapping("/custos")
public class CustoController {

    private final CustoService service;

    public CustoController(CustoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CustoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CustoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<CustoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return service.listarPorVeiculo(veiculoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustoResponse cadastrar(@Valid @RequestBody CustoRequest request) {
        return service.cadastrar(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
