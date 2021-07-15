package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.entities.CohesionMetrics;
import edu.uclm.esi.devopsmetrics.exceptions.CohesionMetricsNotFoundException;
import edu.uclm.esi.devopsmetrics.repositories.CohesionMetricsRepository;

@Service("CohesionMetricsService")
/**
 * @author FcoCrespo
 */
@Transactional
public class CohesionMetricsServiceImpl implements CohesionMetricsService{

	/**
	 * @author FcoCrespo
	 */
	private CohesionMetricsRepository cohesionMetricsRepository;

	public CohesionMetricsServiceImpl(final CohesionMetricsRepository cohesionMetricsRepository) {

		this.cohesionMetricsRepository = cohesionMetricsRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public CohesionMetrics findById(final String id) {

		final Optional<CohesionMetrics> cohesionMetrics = this.cohesionMetricsRepository.findOne(id);

		if (cohesionMetrics.isPresent()) {

			final Optional<CohesionMetrics> cohesionMetricsOpt = cohesionMetrics;

			return cohesionMetricsOpt.get();

		} else {

			throw new CohesionMetricsNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<CohesionMetrics> findAll() {

		final Optional<List<CohesionMetrics>> cohesionMetrics = this.cohesionMetricsRepository.findAll();

		final List<CohesionMetrics> cohesionMetricsList = new ArrayList<CohesionMetrics>();

		if (cohesionMetrics.isPresent()) {
			CohesionMetrics cohesionMetric;
			for (int i = 0; i < cohesionMetrics.get().size(); i++) {
				cohesionMetric = cohesionMetrics.get().get(i);
				cohesionMetricsList.add(cohesionMetric);
			}

			return cohesionMetricsList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveCohesionMetrics(final CohesionMetrics cohesionMetrics) {

		this.cohesionMetricsRepository.saveCohesionMetrics(cohesionMetrics);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateCohesionMetrics(final CohesionMetrics cohesionMetrics) {

		this.cohesionMetricsRepository.updateCohesionMetrics(cohesionMetrics);

	}



}

