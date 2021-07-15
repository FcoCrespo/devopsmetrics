package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.TestMetrics;

/**
 * Interfaz de ClassMetricsRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface TestMetricsRepository {
  /**
   * Método que te devuelve todos los TestMetrics.
   * 
   * @author FcoCrespo
   */
  Optional<List<TestMetrics>> findAll();

  /**
   * Método para guardar un TestMetrics.
   * 
   * @author FcoCrespo
   */
  void saveTestMetrics(TestMetrics testMetrics);

  /**
   * Método para actualizar un testMetrics.
   * 
   * @author FcoCrespo
   */
  void updateTestMetrics(TestMetrics testMetrics);

  /**
   * Método para obtener un TestMetrics por su id.
   * 
   * @author FcoCrespo
   */
  Optional<TestMetrics> findOne(String id);
  
  /**
   * Método para obtener los TestMetrics por su repository y su owner.
   * 
   * @author FcoCrespo
   */
  List<TestMetrics> findAllByTestMetricsAndOwner(String repository, String owner);

  
  /**
   * Método para obtener los TestMetrics por su repository entre unas fechas dadas realizadas por un owner.
   * 
   * @author FcoCrespo
   */
  List<TestMetrics> findAllByTestMetricsBeginEndDateByOwner(String repository, Instant beginDate, Instant endDate, String owner);
}