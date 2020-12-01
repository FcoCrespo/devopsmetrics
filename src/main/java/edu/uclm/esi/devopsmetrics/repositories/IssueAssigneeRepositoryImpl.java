package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.IssueAssignee;

/**
 * Clase que implementa la interfaz IssueAssigneeRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class IssueAssigneeRepositoryImpl implements IssueAssigneeRepository{
	
	/**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public IssueAssigneeRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	@Override
	public Optional<List<IssueAssignee>> findAll() {
		List<IssueAssignee> issuesassignees = this.mongoOperations.find(new Query(), IssueAssignee.class);

	    return Optional.ofNullable(issuesassignees);
	}

	@Override
	public void saveIssueAssignee(IssueAssignee issueAssignee) {
		this.mongoOperations.save(this.mongoOperations.save(issueAssignee));	
	}

	@Override
	public void updateIssueAssignee(IssueAssignee issueAssignee) {
		this.mongoOperations.save(this.mongoOperations.save(issueAssignee));		
	}

	@Override
	public void deleteIssueAssignee(String issueAssigneeId) {
		this.mongoOperations.findAndRemove(new Query(Criteria.where("issue").is(issueAssigneeId)), IssueAssignee.class);
	}

	@Override
	public IssueAssignee findOne(String issueid) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("issue").is(issueid)), IssueAssignee.class);
	}

	@Override
	public IssueAssignee findByAssignee(String usergithub) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("usergithub").is(usergithub)), IssueAssignee.class);
	}

}
