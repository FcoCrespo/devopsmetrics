package edu.uclm.esi.devopsmetrics.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.IssueAssignee;

/**
 * Interfaz de IssueAsigneeRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface IssueAssigneeRepository {
  /**
   * Método que te devuelve todos los issues Assignee.
   * 
   * @author FcoCrespo
   */
  Optional<List<IssueAssignee>> findAll();

  /**
   * Método para guardar un IssueAssignee.
   * 
   * @author FcoCrespo
   */
  void saveIssueAssignee(IssueAssignee issueAssignee);

  /**
   * Método para actualizar un IssueAssignee.
   * 
   * @author FcoCrespo
   */
  void updateIssueAssignee(IssueAssignee issueAssignee);

  /**
   * Método para borrar un issueAssignee.
   * 
   * @author FcoCrespo
   */
  void deleteIssueAssignee(String issueAssigneeId);

  /**
   * Método para obtener un IssueAssignee por su id de issue.
   * 
   * @author FcoCrespo
   */
  IssueAssignee findOne(String issueid);

  /**
   * Método para obtener un IssueRepo por su assigne.
   * 
   * @author FcoCrespo
   */
  IssueAssignee findByAssignee(String usergithub);


}