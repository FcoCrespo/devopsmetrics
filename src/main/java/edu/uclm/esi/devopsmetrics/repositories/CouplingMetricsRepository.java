package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.CouplingMetrics;

/**
 * Interfaz de CouplingMetricsRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface CouplingMetricsRepository {
  /**
   * Método que te devuelve todos los CouplingMetrics.
   * 
   * @author FcoCrespo
   */
  Optional<List<CouplingMetrics>> findAll();

  /**
   * Método para guardar un CouplingMetrics.
   * 
   * @author FcoCrespo
   */
  void saveCouplingMetrics(CouplingMetrics couplingMetrics);

  /**
   * Método para actualizar un CouplingMetrics.
   * 
   * @author FcoCrespo
   */
  void updateCouplingMetrics(CouplingMetrics couplingMetrics);

  /**
   * Método para obtener un couplingMetrics por su id.
   * 
   * @author FcoCrespo
   */
  Optional<CouplingMetrics> findOne(String id);
}