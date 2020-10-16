package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.Commit;

/**
 * Interfaz de CommitRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface CommitRepository {
  /**
   * Método que te devuelve todos los commits.
   * 
   * @author FcoCrespo
   */
  Optional<List<Commit>> findAll();

  /**
   * Método para guardar un commit.
   * 
   * @author FcoCrespo
   */
  void saveCommit(Commit commit);

  /**
   * Método para actualizar un commit.
   * 
   * @author FcoCrespo
   */
  void updateCommit(Commit commit);

  /**
   * Método para borrar un commit.
   * 
   * @author FcoCrespo
   */
  void deleteCommit(String commitId);

  /**
   * Método para obtener un commit por su oid.
   * 
   * @author FcoCrespo
   */
  Optional<Commit> findOne(String oid);

  /**
   * Método para obtener un commit por su oid y su branch.
   * 
   * @author FcoCrespo
   */
  Commit findByOidyBranch(String oid, String branch);
  
  /**
   * Método para obtener un commit por su oid y su branch.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranch(String branch);
  


}