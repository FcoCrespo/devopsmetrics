package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.MethodMetrics;

/**
 * Interfaz de CommitRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface MethodMetricsRepository {
  /**
   * Método que te devuelve todos los MethodMetrics.
   * 
   * @author FcoCrespo
   */
  Optional<List<MethodMetrics>> findAll();

  /**
   * Método para guardar un MethodMetrics.
   * 
   * @author FcoCrespo
   */
  void saveMethodMetrics(MethodMetrics methodMetrics);

  /**
   * Método para actualizar un MethodMetrics.
   * 
   * @author FcoCrespo
   */
  void updateMethodMetrics(MethodMetrics methodMetrics);

  /**
   * Método para obtener un MethodMetrics por su id.
   * 
   * @author FcoCrespo
   */
  Optional<MethodMetrics> findOne(String id);

}