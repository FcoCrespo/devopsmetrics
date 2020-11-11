package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.CommitCursor;

/**
 * Clase que implementa la interfaz CommitCursor.
 * 
 * @author FcoCrespo
 */

@Repository
public class CommitCursorRepositoryImpl implements CommitCursorRepository {
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

	  public CommitCursorRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;

	  }

	  /**
	   * Devuelve todos los CommitCursor.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<CommitCursor>> findAll() {

	    List<CommitCursor> commits = this.mongoOperations.find(new Query(), CommitCursor.class);

	    return Optional.ofNullable(commits);

	  }

	  /**
	   * Devuelve un usuario en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<CommitCursor> findOne(final String endCursor) {
		CommitCursor d = this.mongoOperations.findOne(new Query(Criteria.where("endCursor").is(endCursor)), CommitCursor.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un CommitCursor en la base de datos.
	   * 
	   * @author e3corp
	   */
	  public void saveCommitCursor(final CommitCursor commit) {
	    this.mongoOperations.save(commit);
	  }

	  /**
	   * Actualiza un CommitCursor en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateCommitCursor(final CommitCursor commit) {

	    this.mongoOperations.save(commit);

	  }

	  /**
	   * Borra un commit en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void deleteCommitCursor(final String id) {

	    this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), CommitCursor.class);

	  }

	  @Override
	  public CommitCursor findByEndCursoryHasNextPage(String branch, String repository) {
	    return this.mongoOperations
	        .findOne(new Query(Criteria.where("branch").is(branch)
	        		.and("repository").is(repository)), CommitCursor.class);
	  }

	
}