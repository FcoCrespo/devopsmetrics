package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.ClassMetrics;

/**
 * Clase que implementa la interfaz ClassMetricsRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class ClassMetricsRepositoryImpl implements ClassMetricsRepository {
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

	  public ClassMetricsRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los ClassMetrics.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<ClassMetrics>> findAll() {

	    List<ClassMetrics> classMetrics = this.mongoOperations.find(new Query(), ClassMetrics.class);

	    return Optional.ofNullable(classMetrics);

	  }

	  /**
	   * Devuelve un ClassMetrics en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<ClassMetrics> findOne(final String id) {
		ClassMetrics d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), ClassMetrics.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un ClassMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveClassMetrics(final ClassMetrics classMetrics) {
	    this.mongoOperations.save(classMetrics);
	  }

	  /**
	   * Actualiza un ClassMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateClassMetrics(final ClassMetrics classMetrics) {

	    this.mongoOperations.save(classMetrics);

	  }
	
}