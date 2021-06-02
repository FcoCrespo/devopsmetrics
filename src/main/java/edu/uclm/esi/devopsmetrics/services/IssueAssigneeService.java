package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.IssueAssignee;

public interface IssueAssigneeService {

	/**
	   * Método que te devuelve todos los issues Assignee.
	   * 
	   * @author FcoCrespo
	   */
	  List<IssueAssignee> findAll();

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
	  IssueAssignee getByAssignee(String usergithub);
	  
	  
	  /**
	   * Método para obtener un IssueRepo por su assigne y su issue.
	   * 
	   * @author FcoCrespo
	   */
	  IssueAssignee getByAssigneeAndIssue(String usergithub, String issue);
	  
	  /**
	   * Método para obtener los assignees por el id de su issue asociada
	   * 
	   * @author FcoCrespo
	   */
	  List<IssueAssignee> getAllByIdIssue(String issue);

}
