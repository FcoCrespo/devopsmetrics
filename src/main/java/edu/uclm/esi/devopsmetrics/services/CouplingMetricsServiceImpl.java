package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.CouplingMetricsNotFoundException;
import edu.uclm.esi.devopsmetrics.models.CouplingMetrics;
import edu.uclm.esi.devopsmetrics.repositories.CouplingMetricsRepository;

@Service("CouplingMetricsService")
/**
 * @author FcoCrespo
 */
@Transactional
public class CouplingMetricsServiceImpl implements CouplingMetricsService{

	/**
	 * @author FcoCrespo
	 */
	private CouplingMetricsRepository couplingMetricsRepository;

	public CouplingMetricsServiceImpl(final CouplingMetricsRepository couplingMetricsRepository) {

		this.couplingMetricsRepository = couplingMetricsRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public CouplingMetrics findById(final String id) {

		final Optional<CouplingMetrics> couplingMetrics = this.couplingMetricsRepository.findOne(id);

		if (couplingMetrics.isPresent()) {

			final Optional<CouplingMetrics> couplingMetricsOpt = couplingMetrics;

			return couplingMetricsOpt.get();

		} else {

			throw new CouplingMetricsNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<CouplingMetrics> findAll() {

		final Optional<List<CouplingMetrics>> couplingMetrics = this.couplingMetricsRepository.findAll();

		final List<CouplingMetrics> couplingMetricsList = new ArrayList<CouplingMetrics>();

		if (couplingMetrics.isPresent()) {
			CouplingMetrics couplingMetric;
			for (int i = 0; i < couplingMetrics.get().size(); i++) {
				couplingMetric = couplingMetrics.get().get(i);
				couplingMetricsList.add(couplingMetric);
			}

			return couplingMetricsList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveCouplingMetrics(final CouplingMetrics couplingMetrics) {

		this.couplingMetricsRepository.saveCouplingMetrics(couplingMetrics);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateCouplingMetrics(final CouplingMetrics couplingMetrics) {

		this.couplingMetricsRepository.updateCouplingMetrics(couplingMetrics);

	}



}

