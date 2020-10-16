package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.CommitCursor;

/**
 * Interfaz de CommitCursorRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface CommitCursorRepository {
  /**
   * Método que te devuelve todos los commits.
   * 
   * @author FcoCrespo
   */
  Optional<List<CommitCursor>> findAll();

  /**
   * Método para guardar un commitcursor.
   * 
   * @author FcoCrespo
   */
  void saveCommitCursor(CommitCursor commitCursor);

  /**
   * Método para actualizar un CommitCursor.
   * 
   * @author FcoCrespo
   */
  void updateCommitCursor(CommitCursor commitCursor);

  /**
   * Método para borrar un commitCursor.
   * 
   * @author FcoCrespo
   */
  void deleteCommitCursor(String commitCursorId);

  /**
   * Método para obtener un CommitCursor por su nextCursor.
   * 
   * @author FcoCrespo
   */
  Optional<CommitCursor> findOne(String endCursor);

  /**
   * Método para obtener un commit por su oid y su branch.
   * 
   * @author FcoCrespo
   */
  CommitCursor findByEndCursoryHasNextPage(String branch, String repository);


}