package com.kelvinoliveira.gestaofrota.repository;

import com.kelvinoliveira.gestaofrota.model.StatusVeiculo;
import com.kelvinoliveira.gestaofrota.model.Veiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório de veículos. A placa é consultada ignorando caixa (chave de negócio única).
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlacaIgnoreCase(String placa);

    Optional<Veiculo> findByPlacaIgnoreCase(String placa);

    List<Veiculo> findByStatus(StatusVeiculo status);
}
