package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.CouplingMetrics;

/**
 * Clase que implementa la interfaz CouplingMetricsRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class CouplingMetricsRepositoryImpl implements CouplingMetricsRepository {
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

	  public CouplingMetricsRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los CouplingMetrics.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<CouplingMetrics>> findAll() {

	    List<CouplingMetrics> couplingMetrics = this.mongoOperations.find(new Query(), CouplingMetrics.class);

	    return Optional.ofNullable(couplingMetrics);

	  }

	  /**
	   * Devuelve un CouplingMetrics en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<CouplingMetrics> findOne(final String id) {
		CouplingMetrics d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), CouplingMetrics.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un CouplingMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveCouplingMetrics(final CouplingMetrics couplingMetrics) {
	    this.mongoOperations.save(couplingMetrics);
	  }

	  /**
	   * Actualiza un CouplingMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateCouplingMetrics(final CouplingMetrics couplingMetrics) {

	    this.mongoOperations.save(couplingMetrics);

	  }
	
}