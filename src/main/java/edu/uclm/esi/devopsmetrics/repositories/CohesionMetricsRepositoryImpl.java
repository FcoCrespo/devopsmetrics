package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.entities.CohesionMetrics;

/**
 * Clase que implementa la interfaz CohesionMetricsRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class CohesionMetricsRepositoryImpl implements CohesionMetricsRepository {
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

	  public CohesionMetricsRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los CohesionMetrics.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<CohesionMetrics>> findAll() {

	    List<CohesionMetrics> cohesionMetrics = this.mongoOperations.find(new Query(), CohesionMetrics.class);

	    return Optional.ofNullable(cohesionMetrics);

	  }

	  /**
	   * Devuelve un ClassMetrics en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<CohesionMetrics> findOne(final String id) {
		CohesionMetrics d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), CohesionMetrics.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un CohesionMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveCohesionMetrics(final CohesionMetrics cohesionMetrics) {
	    this.mongoOperations.save(cohesionMetrics);
	  }

	  /**
	   * Actualiza un ClassMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateCohesionMetrics(final CohesionMetrics cohesionMetrics) {

	    this.mongoOperations.save(cohesionMetrics);

	  }

}