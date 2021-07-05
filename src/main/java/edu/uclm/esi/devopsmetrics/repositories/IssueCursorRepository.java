package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.IssueCursor;

/**
 * Interfaz de IssueCursorRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface IssueCursorRepository{
  /**
   * Método que te devuelve todos los issues cursor.
   * 
   * @author FcoCrespo
   */
  Optional<List<IssueCursor>> findAll();

  /**
   * Método para guardar un IssueCursor.
   * 
   * @author FcoCrespo
   */
  void saveIssueCursor(IssueCursor issueCursor);

  /**
   * Método para actualizar un CommitCursor.
   * 
   * @author FcoCrespo
   */
  void updateIssueCursor(IssueCursor issueCursor);

  /**
   * Método para borrar un IssueCursor.
   * 
   * @author FcoCrespo
   */
  void deleteIssueCursor(String issueCursorId);

  /**
   * Método para obtener un IssueCursor por su nextCursor.
   * 
   * @author FcoCrespo
   */
  IssueCursor findOne(String endCursor);

  /**
   * Método para obtener un IssueCursor por su repository.
   * 
   * @author FcoCrespo
   */
  IssueCursor findByRepositoryAndOwner(String repository, String owner);


}