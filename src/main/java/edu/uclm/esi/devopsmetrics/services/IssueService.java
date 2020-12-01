package edu.uclm.esi.devopsmetrics.services;

import java.time.Instant;
import java.util.List;

import edu.uclm.esi.devopsmetrics.models.Issue;

public interface IssueService {
	/**
	   * Método que te devuelve todos los issues.
	   * 
	   * @author FcoCrespo
	   */
	  List<Issue> findAll();

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
	  Issue getByIdyState(String id, String state);
	  
	  
	  /**
	   * Método para obtener los issues por su state.
	   * 
	   * @author FcoCrespo
	   */
	  List<Issue> getAllByState(String state);
	  
	  
	  /**
	   * Método para obtener los issues entre unas fechas dadas.
	   * 
	   * @author FcoCrespo
	   */
	  List<Issue> getAllByBranchBeginEndDate(Instant beginDate, Instant endDate);

}
