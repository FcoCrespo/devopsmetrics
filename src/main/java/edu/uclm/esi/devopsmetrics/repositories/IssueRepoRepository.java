package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.IssueRepo;

/**
 * Interfaz de IssueRepoRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface IssueRepoRepository {
	
  
  /**
   * Método que te devuelve todos los issues repo.
   * 
   * @author FcoCrespo
   */
  Optional<List<IssueRepo>> findAll();

  /**
   * Método para guardar un IssueCursor.
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
   * Método para obtener un IssueRepo por su repository y owner.
   * 
   * @author FcoCrespo
   */
  IssueRepo findByRepoyOwner(String repository, String owner);


}