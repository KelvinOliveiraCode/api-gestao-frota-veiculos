package com.kelvinoliveira.gestaofrota.repository;

import com.kelvinoliveira.gestaofrota.model.Manutencao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório de ordens de manutenção.
 */
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    List<Manutencao> findByVeiculoId(Long veiculoId);

    List<Manutencao> findByDataBetween(LocalDate inicio, LocalDate fim);
}
