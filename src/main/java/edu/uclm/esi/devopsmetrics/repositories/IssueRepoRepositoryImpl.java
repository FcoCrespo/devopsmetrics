package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.IssueRepo;

/**
 * Clase que implementa la interfaz IssueRepoRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class IssueRepoRepositoryImpl implements IssueRepoRepository{
	
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

	  public IssueRepoRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	@Override
	public Optional<List<IssueRepo>> findAll() {
		List<IssueRepo> issuesrepo = this.mongoOperations.find(new Query(), IssueRepo.class);

	    return Optional.ofNullable(issuesrepo);
	}

	@Override
	public void saveIssueRepo(IssueRepo issueRepo) {
		this.mongoOperations.save(this.mongoOperations.save(issueRepo));
	}

	@Override
	public void updateIssueRepo(IssueRepo issueRepo) {
		this.mongoOperations.save(this.mongoOperations.save(issueRepo));	
	}

	@Override
	public void deleteIssueRepo(String issueRepoId) {
		this.mongoOperations.findAndRemove(new Query(Criteria.where("issue").is(issueRepoId)), IssueRepo.class);
	}

	@Override
	public IssueRepo findOne(String issueid) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("id").is(issueid)), IssueRepo.class);
	}

	@Override
	public List<IssueRepo> findByRepoyOwner(String repository, String owner) {
		return this.mongoOperations.find(new Query(Criteria.where("repository").is(repository).and("owner").is(owner)), IssueRepo.class);
	}
	
	@Override
	public IssueRepo findOneByRepoyOwner(String repository, String owner) {
		return this.mongoOperations.findOne(new Query(Criteria.where("repository").is(repository).and("owner").is(owner)), IssueRepo.class);
	}

}
