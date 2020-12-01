package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.Issue;

/**
 * Clase que implementa la interfaz IssueRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class IssueRepositoryImpl implements IssueRepository{
	  
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

	  public IssueRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	@Override
	public Optional<List<Issue>> findAll() {
		List<Issue> issues = this.mongoOperations.find(new Query(), Issue.class);

	    return Optional.ofNullable(issues);
	}

	@Override
	public void saveIssue(Issue issue) {
		this.mongoOperations.save(issue);	
	}

	@Override
	public void updateIssue(Issue issue) {
		this.mongoOperations.save(issue);
	}

	@Override
	public void deleteIssue(String issueId) {
		this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(issueId)), Issue.class);		
	}

	@Override
	public Issue findOne(String id) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("id").is(id)), Issue.class);
	}

	@Override
	public Issue findByIdyState(String id, String state) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("id").is(id).and("state").is(state)), Issue.class);
	}

	@Override
	public Optional<List<Issue>> findAllByState(String state) {
		List<Issue> issues = this.mongoOperations.find(new Query(Criteria.where("state").is(state)), Issue.class);

	    return Optional.ofNullable(issues);
	}

	@Override
	public List<Issue> findAllByBranchBeginEndDate(Instant beginDate, Instant endDate) {
		return this.mongoOperations
		        .find(new Query(Criteria.where("createdAt").gte(beginDate).and("closedAt").lte(endDate)), Issue.class);
	}

}
