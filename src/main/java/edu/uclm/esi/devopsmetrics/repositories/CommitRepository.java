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
   * Método para borrar commits de una rama.
   * 
   * @author FcoCrespo
   */
  void deleteCommits(String branchId);

  /**
   * Método para obtener un commit por su oid.
   * 
   * @author FcoCrespo
   */
  Optional<Commit> findOne(String oid);

  /**
   * Método para obtener un commit por su oid y su branchId.
   * 
   * @author FcoCrespo
   */
  Commit findByOidyBranch(String oid, String branchId);
  
  /**
   * Método para obtener un commit por su branchId.
   * 
   * @author FcoCrespo
   */
  Commit findByBranch(String branchId);
  
  /**
   * Método para obtener los commits por su branchId de repositorio.
   * 
   * @author FcoCrespo
   */
  Optional<List<Commit>> findAllByBranch(String branchId);
  
  /**
   * Método para obtener los commits por su branchId de repositorio y su autor.
   * 
   * @author FcoCrespo
   */
  Optional<List<Commit>> findAllByBranchAndUserGithub(String branchId, String usergithub);
  
  
  /**
   * Método para obtener si existe por su branchId de repositorio y su autor.
   * 
   * @author FcoCrespo
   */
  Commit findByBranchAndUserGithub(String branchId, String usergithub);
  
  /**
   * Método para obtener el ultimo commit por fecha.
   * 
   * @author FcoCrespo
   */
  Commit findLastCommit(String branchId);
  
  /**
   * Método para obtener los commits por su branchId de repositorio entre unas fechas dadas.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranchBeginEndDate(String branch, Instant beginDate, Instant endDate);
  
  /**
   * Método para obtener los commits por su branchId de repositorio entre unas fechas dadas realizadas por un autor.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByBranchBeginEndDateByAuthor(String branch, Instant beginDate, Instant endDate, String githubuser);


  /**
   * Método para obtener los commits  de repositorio entre unas fechas dadas realizadas por un autor.
   * 
   * @author FcoCrespo
   */
  List<Commit> findAllByAuthor(String githubuser);



}