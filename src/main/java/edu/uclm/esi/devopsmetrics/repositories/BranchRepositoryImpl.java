package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.entities.Branch;

/**
 * Clase que implementa la interfaz CommitCursor.
 * 
 * @author FcoCrespo
 */

@Repository
public class BranchRepositoryImpl implements BranchRepository {
	  /**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;
	  private String repositoryString;
	  

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public BranchRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	    this.repositoryString = "repository";
	  }

	  /**
	   * Devuelve todos los CommitCursor.
	   * 
	   * @author FcoCrespo
	   */
	  public List<Branch> findAll() {

	    return this.mongoOperations.find(new Query(), Branch.class);

	  }

	  /**
	   * Guarda un Branch en la base de datos.
	   * 
	   * @author e3corp
	   */
	  public void saveBranch(final Branch branch) {
	    this.mongoOperations.save(branch);
	  }

	  /**
	   * Actualiza un branch en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateBranch(final Branch branch) {

	    this.mongoOperations.save(branch);

	  }

	  /**
	   * Borra un branch en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void deleteBranch(final String id) {

	    this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), Branch.class);

	  }

	  @Override
	  public Branch findByRepositoryOwnerAndName(final String reponame, final String owner, final String name) {
	    return this.mongoOperations.findOne(new Query(Criteria.where(this.repositoryString).is(reponame)
	    		.and("owner").is(owner).and("name").is(name)), Branch.class);	 
	  }

	@Override
	public Optional<Branch> findOne(String idGithub) {
		Branch d = this.mongoOperations.findOne(new Query(Criteria.where("idGithub").is(idGithub)), Branch.class);
	    return Optional.ofNullable(d);
	    
	}
	
	@Override
	public List<Branch> findAllbyRepositoryAndOwner(String repository, String owner) {
		return this.mongoOperations.find(new Query(Criteria.where(this.repositoryString).is(repository).and("owner").is(owner)), Branch.class);
	}

	

	@Override
	public Branch findBeforeBranchByOrder(String repository, int order) {
		return this.mongoOperations.findOne(new Query(Criteria.where(this.repositoryString)
				.is(repository).and("order").is(order-1)), Branch.class);
		
		
	}

	@Override
	public Optional<Branch> findOneByName(String name) {
		Branch d = this.mongoOperations.findOne(new Query(Criteria.where("name").is(name)), Branch.class);
	    return Optional.ofNullable(d);
	}

	
}