package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.MethodMetricsNotFoundException;
import edu.uclm.esi.devopsmetrics.models.MethodMetrics;
import edu.uclm.esi.devopsmetrics.repositories.MethodMetricsRepository;

@Service("MethodMetricsService")
/**
 * @author FcoCrespo
 */
@Transactional
public class MethodMetricsServiceImpl implements MethodMetricsService {

	/**
	 * @author FcoCrespo
	 */
	private MethodMetricsRepository methodMetricsRepository;

	public MethodMetricsServiceImpl(final MethodMetricsRepository methodMetricsRepository) {

		this.methodMetricsRepository = methodMetricsRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public MethodMetrics findById(final String id) {

		final Optional<MethodMetrics> methodMetrics = this.methodMetricsRepository.findOne(id);

		if (methodMetrics.isPresent()) {

			final Optional<MethodMetrics> methodMetricsOpt = methodMetrics;

			return methodMetricsOpt.get();

		} else {

			throw new MethodMetricsNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<MethodMetrics> findAll() {

		final Optional<List<MethodMetrics>> methodsMetrics = this.methodMetricsRepository.findAll();

		final List<MethodMetrics> methodMetricsList = new ArrayList<MethodMetrics>();

		if (methodsMetrics.isPresent()) {
			MethodMetrics methodsMetric;
			for (int i = 0; i < methodsMetrics.get().size(); i++) {
				methodsMetric = methodsMetrics.get().get(i);
				methodMetricsList.add(methodsMetric);
			}

			return methodMetricsList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveMethodMetrics(final MethodMetrics methodMetrics) {

		this.methodMetricsRepository.saveMethodMetrics(methodMetrics);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateMethodMetrics(final MethodMetrics methodMetrics) {

		this.methodMetricsRepository.updateMethodMetrics(methodMetrics);

	}



}
