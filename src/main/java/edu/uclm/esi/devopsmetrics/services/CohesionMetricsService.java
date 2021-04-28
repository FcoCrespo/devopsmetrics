package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.CohesionMetrics;

/**
 * @author FcoCrespo
 */
public interface CohesionMetricsService {
	/**
	 * @author FcoCrespo
	 */
	List<CohesionMetrics> findAll();

	/**
	 * @author FcoCrespo
	 */
	CohesionMetrics findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveCohesionMetrics(CohesionMetrics cohesionMetrics);

	/**
	 * @author FcoCrespo
	 */
	void updateCohesionMetrics(CohesionMetrics cohesionMetrics);

}
