package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.Issue;

/**
 * Interfaz de IssueRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface IssueRepository {
  /**
   * Método que te devuelve todos los issues.
   * 
   * @author FcoCrespo
   */
  Optional<List<Issue>> findAll();

  /**
   * Método para guardar un issue.
   * 
   * @author FcoCrespo
   */
  void saveIssue(Issue issue);

  /**
   * Método para actualizar un Issue.
   * 
   * @author FcoCrespo
   */
  void updateIssue(Issue issue);

  /**
   * Método para borrar Issues de un repositorio por su id.
   * 
   * @author FcoCrespo
   */
  void deleteIssue(String issueId);

  /**
   * Método para obtener un issue por su id.
   * 
   * @author FcoCrespo
   */
  Issue findOne(String id);

  /**
   * Método para obtener un issue por su id y su estado.
   * 
   * @author FcoCrespo
   */
  Issue findByIdyState(String id, String state);
  
  
  /**
   * Método para obtener los issues por su state.
   * 
   * @author FcoCrespo
   */
  Optional<List<Issue>> findAllByState(String state);
  
  
  /**
   * Método para obtener los issues entre unas fechas dadas.
   * 
   * @author FcoCrespo
   */
  List<Issue> findAllByBranchBeginEndDate(Instant beginDate, Instant endDate);
  
}