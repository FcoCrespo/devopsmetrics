package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
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
  void deleteCommit(String repository);

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
   * Método para obtener commits por su reponame y su branch.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranch(String reponame, String branch);
  
  /**
   * Método para obtener commits por su reponame, su branch y el nombre del autor.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranchAndAuthorName(String reponame, String branch, String authorName);
  
  /**
   * Método para obtener commits por su fecha de inicio y de fin
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranchBeginEndDate(String reponame, String branch, Instant beginDate, Instant endDate);
  
  /**
   * Método para obtener commits por su fecha de inicio y de fin
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranchAuthorBeginEndDate(String reponame, String branch, String authorName, Instant beginDate, Instant endDate);
  
  /**
   * Método para comprobar si existen commits de un repositorio en la BBDD
   * 
   * @author FcoCrespo
   */
  Commit findRepository(String reponame);


}