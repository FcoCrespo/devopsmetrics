package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.MethodTest;

/**
 * Clase que implementa la interfaz MethodTestRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class MethodTestRepositoryImpl implements MethodTestRepository {
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

	  public MethodTestRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los MethodTest.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<MethodTest>> findAll() {

	    List<MethodTest> methodTest = this.mongoOperations.find(new Query(), MethodTest.class);

	    return Optional.ofNullable(methodTest);

	  }

	  /**
	   * Devuelve un MethodTest en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<MethodTest> findOne(final String id) {
		MethodTest d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), MethodTest.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un MethodTest en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveMethodTest(final MethodTest methodTest) {
	    this.mongoOperations.save(methodTest);
	  }

	  /**
	   * Actualiza un methodTest en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateMethodTest(final MethodTest methodTest) {

	    this.mongoOperations.save(methodTest);

	  }     		
	
}