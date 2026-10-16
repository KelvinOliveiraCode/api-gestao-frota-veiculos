package com.kelvinoliveira.gestaofrota.repository;

import com.kelvinoliveira.gestaofrota.model.Custo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório de custos da frota.
 */
public interface CustoRepository extends JpaRepository<Custo, Long> {

    List<Custo> findByVeiculoId(Long veiculoId);

    List<Custo> findByVeiculoIdAndDataBetween(Long veiculoId, LocalDate inicio, LocalDate fim);

    List<Custo> findByDataBetween(LocalDate inicio, LocalDate fim);
}
