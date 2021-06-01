package edu.uclm.esi.devopsmetrics.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.TestMetrics;

/**
 * Clase que implementa la interfaz TestMetricsRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class TestMetricsRepositoryImpl implements TestMetricsRepository {
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

	  public TestMetricsRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;
	  }

	  /**
	   * Devuelve todos los TestMetrics.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<TestMetrics>> findAll() {

	    List<TestMetrics> testMetrics = this.mongoOperations.find(new Query(), TestMetrics.class);

	    return Optional.ofNullable(testMetrics);

	  }

	  /**
	   * Devuelve un TestMetrics en función de su oid.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<TestMetrics> findOne(final String id) {
		TestMetrics d = this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), TestMetrics.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un ClassMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void saveTestMetrics(final TestMetrics testMetrics) {
	    this.mongoOperations.save(testMetrics);
	  }

	  /**
	   * Actualiza un TestMetrics en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateTestMetrics(final TestMetrics testMetrics) {

	    this.mongoOperations.save(testMetrics);

	  }


	@Override
	public List<TestMetrics> findAllByTestMetricsBeginEndDateByOwner(String repository, Instant beginDate,
			Instant endDate, String owner) {
		return this.mongoOperations
		        .find(new Query(Criteria.where("repository").is(repository).
		        		and("dateTest").gte(beginDate).lte(endDate).and("owner").is(owner)), TestMetrics.class);
	}

	@Override
	public List<TestMetrics> findAllByTestMetricsAndOwner(String repository, String owner) {
		return this.mongoOperations
		        .find(new Query(Criteria.where("repository").is(repository).
		        		and("owner").is(owner)), TestMetrics.class);
	
	}
	
}