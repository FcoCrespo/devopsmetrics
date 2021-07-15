package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.CohesionMetrics;

/**
 * Interfaz de CohesionMetrics.
 * 
 * @author FcoCrespo
 */
@Repository
public interface CohesionMetricsRepository {
  /**
   * Método que te devuelve todos los CohesionMetrics.
   * 
   * @author FcoCrespo
   */
  Optional<List<CohesionMetrics>> findAll();

  /**
   * Método para guardar un CohesionMetrics.
   * 
   * @author FcoCrespo
   */
  void saveCohesionMetrics(CohesionMetrics cohesionMetrics);

  /**
   * Método para actualizar un cohesionMetrics.
   * 
   * @author FcoCrespo
   */
  void updateCohesionMetrics(CohesionMetrics cohesionMetrics);

  /**
   * Método para obtener un cohesionMetrics por su id.
   * 
   * @author FcoCrespo
   */
  Optional<CohesionMetrics> findOne(String id);
}