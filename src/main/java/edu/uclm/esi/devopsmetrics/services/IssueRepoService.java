package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.IssueRepo;

public interface IssueRepoService {
	

	  /**
	   * Método que te devuelve todos los issues repo.
	   * 
	   * @author FcoCrespo
	   */
	  List<IssueRepo> findAll();

	  /**
	   * Método para guardar un IssueRepo.
	   * 
	   * @author FcoCrespo
	   */
	  void saveIssueRepo(IssueRepo issueRepo);

	  /**
	   * Método para actualizar un IssueRepo.
	   * 
	   * @author FcoCrespo
	   */
	  void updateIssueRepo(IssueRepo issueRepo);

	  /**
	   * Método para borrar un IssueRepo.
	   * 
	   * @author FcoCrespo
	   */
	  void deleteIssueRepo(String issueRepoId);

	  /**
	   * Método para obtener un IssueRepo por su id de issue.
	   * 
	   * @author FcoCrespo
	   */
	  IssueRepo findOne(String issueid);

	  /**
	   * Método para obtener la lista de IssueRepo por su repository y owner.
	   * 
	   * @author FcoCrespo
	   */
	  List<IssueRepo> getByRepoyOwner(String repository, String owner);
	  
	  /**
	   * Método para obtener un IssueRepo por su repository y owner.
	   * 
	   * @author FcoCrespo
	   */
	  IssueRepo getOneByRepoyOwner(String repository, String owner);


}
