package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;

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

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public BranchRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;

	  }

	  /**
	   * Devuelve todos los CommitCursor.
	   * 
	   * @author FcoCrespo
	   */
	  public List<Branch> findAll() {

	    List<Branch> branches = this.mongoOperations.find(new Query(), Branch.class);
	
	    return branches;

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
	  public Branch findByRepositoryyName(final String repository, final String name) {
	    Branch branch = this.mongoOperations
	        .findOne(new Query(Criteria.where("repository").is(repository)
	        		.and("name").is(name)), Branch.class);
	    return branch;
	  }

	@Override
	public Optional<Branch> findOne(String idGithub) {
		Branch d = this.mongoOperations.findOne(new Query(Criteria.where("idGithub").is(idGithub)), Branch.class);
	    Optional<Branch> branch = Optional.ofNullable(d);
	    return branch;
	}

	@Override
	public List<Branch> findAllbyRepository(String repository) {
		List<Branch> listBranches = this.mongoOperations.find(new Query(Criteria.where("repository").is(repository)), Branch.class);
	    return listBranches;
	}

	
}