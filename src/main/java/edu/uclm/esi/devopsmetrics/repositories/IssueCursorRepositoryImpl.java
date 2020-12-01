package edu.uclm.esi.devopsmetrics.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.IssueCursor;

/**
 * Clase que implementa la interfaz IssueCursorRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class IssueCursorRepositoryImpl implements IssueCursorRepository{
	
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

	  public IssueCursorRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	@Override
	public Optional<List<IssueCursor>> findAll() {
		 List<IssueCursor> issuescursor = this.mongoOperations.find(new Query(), IssueCursor.class);

		    return Optional.ofNullable(issuescursor);
	}

	@Override
	public void saveIssueCursor(IssueCursor issueCursor) {
		this.mongoOperations.save(issueCursor);
		
	}

	@Override
	public void updateIssueCursor(IssueCursor issueCursor) {
		this.mongoOperations.save(issueCursor);
		
	}

	@Override
	public void deleteIssueCursor(String issueCursorId) {
	    this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(issueCursorId)), IssueCursor.class);
		
	}

	@Override
	public IssueCursor findOne(String endCursor) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("endCursor").is(endCursor)), IssueCursor.class);
	}

	@Override
	public IssueCursor findByRepository(String repository) {
		return this.mongoOperations
		        .findOne(new Query(Criteria.where("repository").is(repository)), IssueCursor.class);
	}

}
