package edu.uclm.esi.devopsmetrics.services;

import java.time.Instant;
import java.util.List;

import edu.uclm.esi.devopsmetrics.models.TestMetrics;

/**
 * @author FcoCrespo
 */
public interface TestMetricsService {
	/**
	 * @author FcoCrespo
	 */
	List<TestMetrics> findAll();

	/**
	 * @author FcoCrespo
	 */
	TestMetrics findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveTestMetrics(TestMetrics testMetrics);

	/**
	 * @author FcoCrespo
	 */
	void updateTestMetrics(TestMetrics testMetrics);
	
	/**
	 * @author FcoCrespo
	 */
	List<TestMetrics> getAllByRepositoryBeginEndDateByOwner(String repository, Instant beginDate, Instant endDate, String owner);

}
