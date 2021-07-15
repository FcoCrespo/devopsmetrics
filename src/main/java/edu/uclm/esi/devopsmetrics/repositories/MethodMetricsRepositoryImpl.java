package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.entities.MethodMetrics;

/**
 * Clase que implementa la interfaz MethodMetricsRepositoryImpl.
 * 
 * @author FcoCrespo
 */

@Repository
public class MethodMetricsRepositoryImpl implements MethodMetricsRepository {
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

	  public MethodMetricsRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los MethodMetrics.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<MethodMetrics>> findAll() {

	    List<MethodMetrics> methodMetrics = this.mongoOperations.find(new Query(), MethodMetrics.class);

	    return Optional.ofNullable(methodMetrics);

	  }

	  /**
	   * Devuelve un MethodMetrics en función de su id.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<MethodMetrics> findOne(final String id) {
		MethodMetrics d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), MethodMetrics.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un MethodMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveMethodMetrics(final MethodMetrics methodMetrics) {
	    this.mongoOperations.save(methodMetrics);
	  }

	  /**
	   * Actualiza un MethodMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateMethodMetrics(final MethodMetrics methodMetrics) {

	    this.mongoOperations.save(methodMetrics);

	  }
	
}