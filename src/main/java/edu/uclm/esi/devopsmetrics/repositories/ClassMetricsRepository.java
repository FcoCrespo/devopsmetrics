package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.ClassMetrics;

/**
 * Interfaz de ClassMetricsRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface ClassMetricsRepository {
  /**
   * Método que te devuelve todos los ClassMetrics.
   * 
   * @author FcoCrespo
   */
  Optional<List<ClassMetrics>> findAll();

  /**
   * Método para guardar un ClassMetrics.
   * 
   * @author FcoCrespo
   */
  void saveClassMetrics(ClassMetrics classMetrics);

  /**
   * Método para actualizar un ClassMetrics.
   * 
   * @author FcoCrespo
   */
  void updateClassMetrics(ClassMetrics classMetrics);

  /**
   * Método para obtener un ClassMetrics por su id.
   * 
   * @author FcoCrespo
   */
  Optional<ClassMetrics> findOne(String id);
}