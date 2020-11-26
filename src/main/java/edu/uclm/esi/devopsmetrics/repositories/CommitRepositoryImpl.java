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

import edu.uclm.esi.devopsmetrics.models.Commit;

/**
 * Clase que implementa la interfaz CommitRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class CommitRepositoryImpl implements CommitRepository {
	  /**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;
	  private String branchString;

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public CommitRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	    this.branchString = "branchId";
	  }

	  /**
	   * Devuelve todos los commits.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<Commit>> findAll() {

	    List<Commit> commits = this.mongoOperations.find(new Query(), Commit.class);

	    return Optional.ofNullable(commits);

	  }

	  /**
	   * Devuelve un usuario en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<Commit> findOne(final String oid) {
	    Commit d = this.mongoOperations.findOne(new Query(Criteria.where("oid").is(oid)), Commit.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un commit en la base de datos.
	   * 
	   * @author e3corp
	   */
	  public void saveCommit(final Commit commit) {
	    this.mongoOperations.save(commit);
	  }

	  /**
	   * Actualiza un commit en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateCommit(final Commit commit) {

	    this.mongoOperations.save(commit);

	  }

	  @Override
	  public Commit findByOidyBranch(final String oid, final String branch) {
		return this.mongoOperations
	        .findOne(new Query(Criteria.where("oid").is(oid).and(this.branchString).is(branch)), Commit.class);
	  }

	@Override
	public void deleteCommits(String branchId) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where(this.branchString).is(branchId)), Commit.class);
	}

	@Override
	public Commit findByBranch(String branchId) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where(this.branchString).is(branchId)), Commit.class);
	}

	@Override
	public Optional<List<Commit>> findAllByBranch(String branchId) {
		List<Commit> commits = this.mongoOperations.find(new Query(Criteria.where(this.branchString).is(branchId)), Commit.class);

	    return Optional.ofNullable(commits);
	}

	@Override
	public Optional<List<Commit>> findAllByBranchAndUserGithub(String branchId, String usergithub) {
		List<Commit> commits = this.mongoOperations.find(new Query(Criteria.where(this.branchString).is(branchId).and("usergithub").is(usergithub)), Commit.class);

	    return Optional.ofNullable(commits);
	}

	@Override
	public List<Commit> findAllByBranchBeginEndDate(String branch, Instant beginDate,
			Instant endDate) {
		
		return this.mongoOperations
		        .find(new Query(Criteria.where(this.branchString).is(branch).
		        		and("pushedDate").gte(beginDate).lte(endDate)), Commit.class);
	}
	  
	@Override
	public List<Commit> findAllByBranchBeginEndDateByAuthor(String branch, Instant beginDate,
			Instant endDate, String usergithub) {
		
		return this.mongoOperations
		        .find(new Query(Criteria.where(this.branchString).is(branch).
		        		and("pushedDate").gte(beginDate).lte(endDate).and("usergithub").is(usergithub)), Commit.class);
	}
	

	
}