package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.entities.TokenGithub;

@Repository
public class TokenGithubRepositoryImpl implements TokenGithubRepository{

	 /**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;
	  private  final String secretTStr;

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public TokenGithubRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	    this.secretTStr="secretT";

	  }

	  /**
	   * Devuelve todos los TokenGithub.
	   * 
	   * @author FcoCrespo
	   */
	  public List<TokenGithub> findAll() {

	    return this.mongoOperations.find(new Query(), TokenGithub.class);
	    
	  }

	  /**
	   * Devuelve un TokenGithub en función de su TokenGithub.
	   * 
	   * @author FcoCrespo
	   */
	  public TokenGithub findByOwner(final String owner) {
		return this.mongoOperations.findOne(new Query(Criteria.where("owner").is(owner)), TokenGithub.class);

	  }

	  /**
	   * Guarda un TokenGithub en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveTokenGithub(final TokenGithub tokenGithub) {
	    this.mongoOperations.save(tokenGithub);
	  }

	  /**
	   * Actualiza un TokenGithub en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateTokenGithub(final TokenGithub tokenGithub) {

	    this.mongoOperations.save(tokenGithub);

	  }

	  /**
	   * Borra un TokenGithub en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void deleteTokenGithub(final String secretT) {

	    this.mongoOperations.findAndRemove(new Query(Criteria.where(this.secretTStr).is(secretT)), TokenGithub.class);

	  }

	@Override
	public TokenGithub findByOwnerAndToken(String owner, String secretT) {
		 return this.mongoOperations
			        .findOne(new Query(Criteria.where("owner").is(owner).and(this.secretTStr).is(secretT)), TokenGithub.class);
			
	}
}
