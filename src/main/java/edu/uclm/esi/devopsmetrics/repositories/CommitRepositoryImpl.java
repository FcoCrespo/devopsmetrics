package edu.uclm.esi.devopsmetrics.repositories;

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

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public CommitRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;

	  }

	  /**
	   * Devuelve todos los commits.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<Commit>> findAll() {

	    List<Commit> commits = this.mongoOperations.find(new Query(), Commit.class);

	    Optional<List<Commit>> optionalCommits = Optional.ofNullable(commits);

	    return optionalCommits;

	  }

	  /**
	   * Devuelve un usuario en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<Commit> findOne(final String oid) {
	    Commit d = this.mongoOperations.findOne(new Query(Criteria.where("oid").is(oid)), Commit.class);
	    Optional<Commit> commit = Optional.ofNullable(d);
	    return commit;
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

	  /**
	   * Borra un commit en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void deleteCommit(final String id) {

	    this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), Commit.class);

	  }

	  @Override
	  public Commit findByOidyBranch(final String oid, final String branch) {
		Commit commit = this.mongoOperations
	        .findOne(new Query(Criteria.where("oid").is(oid).and("branch").is(branch)), Commit.class);
	    return commit;
	  }
	  
	  @Override
	  public List<Commit> findAllByBranch(final String reponame, final String branch) {
		List<Commit> commits = this.mongoOperations
	        .find(new Query(Criteria.where("branch").is(branch).and("repository").is(reponame)), Commit.class);
	    return commits;
	  }

	@Override
	public List<Commit> findAllByBranchAndAuthorName(String reponame, String branch, String authorName) {
		System.out.println("repo: "+authorName);
		List<Commit> commits = this.mongoOperations
		        .find(new Query(Criteria.where("branch").is(branch).
		        		and("repository").is(reponame).
		        		and("authorName").is(authorName)), Commit.class);
		System.out.println("size aqui: "+commits.size());
	    return commits;
	}

	
}