package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.MethodTest;
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
  
  /**
   * Método para obtener los TestMetrics por el Id del test en el que se realizaron
   * 
   * @author FcoCrespo
   */
  List<MethodTest> findAllByAllByTestId(String idTest);
}