package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.entities.CouplingMetrics;

/**
 * @author FcoCrespo
 */
public interface CouplingMetricsService {
	/**
	 * @author FcoCrespo
	 */
	List<CouplingMetrics> findAll();

	/**
	 * @author FcoCrespo
	 */
	CouplingMetrics findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveCouplingMetrics(CouplingMetrics couplingMetrics);

	/**
	 * @author FcoCrespo
	 */
	void updateCouplingMetrics(CouplingMetrics couplingMetrics);

}
