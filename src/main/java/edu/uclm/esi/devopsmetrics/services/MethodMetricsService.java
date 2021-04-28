package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.MethodMetrics;

/**
 * @author FcoCrespo
 */
public interface MethodMetricsService {
	/**
	 * @author FcoCrespo
	 */
	List<MethodMetrics> findAll();

	/**
	 * @author FcoCrespo
	 */
	MethodMetrics findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveMethodMetrics(MethodMetrics methodMetrics);

	/**
	 * @author FcoCrespo
	 */
	void updateMethodMetrics(MethodMetrics methodMetrics);

}