package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.ClassMetrics;

/**
 * @author FcoCrespo
 */
public interface ClassMetricsService {
	/**
	 * @author FcoCrespo
	 */
	List<ClassMetrics> findAll();

	/**
	 * @author FcoCrespo
	 */
	ClassMetrics findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveClassMetrics(ClassMetrics classMetrics);

	/**
	 * @author FcoCrespo
	 */
	void updateClassMetrics(ClassMetrics classMetrics);

}