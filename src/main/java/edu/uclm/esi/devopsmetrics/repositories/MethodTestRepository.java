package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import edu.uclm.esi.devopsmetrics.models.MethodTest;

/**
 * Interfaz de MethodTestRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface MethodTestRepository {
  /**
   * Método que te devuelve todos los MethodTest.
   * 
   * @author FcoCrespo
   */
  Optional<List<MethodTest>> findAll();

  /**
   * Método para guardar un MethodTest.
   * 
   * @author FcoCrespo
   */
  void saveMethodTest(MethodTest methodTest);

  /**
   * Método para actualizar un methodTest.
   * 
   * @author FcoCrespo
   */
  void updateMethodTest(MethodTest methodTest);

  /**
   * Método para obtener un MethodTest por su id.
   * 
   * @author FcoCrespo
   */
  Optional<MethodTest> findOne(String id);

}