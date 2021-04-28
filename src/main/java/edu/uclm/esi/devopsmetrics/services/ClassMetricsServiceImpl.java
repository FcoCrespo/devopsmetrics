package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.ClassMetricsNotFoundException;
import edu.uclm.esi.devopsmetrics.models.ClassMetrics;
import edu.uclm.esi.devopsmetrics.repositories.ClassMetricsRepository;

@Service("ClassMetricsService")
/**
 * @author FcoCrespo
 */
@Transactional
public class ClassMetricsServiceImpl implements ClassMetricsService{

	/**
	 * @author FcoCrespo
	 */
	private ClassMetricsRepository classMetricsRepository;

	public ClassMetricsServiceImpl(final ClassMetricsRepository classMetricsRepository) {

		this.classMetricsRepository = classMetricsRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public ClassMetrics findById(final String id) {

		final Optional<ClassMetrics> classMetrics = this.classMetricsRepository.findOne(id);

		if (classMetrics.isPresent()) {

			final Optional<ClassMetrics> classMetricsOpt = classMetrics;

			return classMetricsOpt.get();

		} else {

			throw new ClassMetricsNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<ClassMetrics> findAll() {

		final Optional<List<ClassMetrics>> classMetrics = this.classMetricsRepository.findAll();

		final List<ClassMetrics> classMetricsList = new ArrayList<ClassMetrics>();

		if (classMetrics.isPresent()) {
			ClassMetrics classMetric;
			for (int i = 0; i < classMetrics.get().size(); i++) {
				classMetric = classMetrics.get().get(i);
				classMetricsList.add(classMetric);
			}

			return classMetricsList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveClassMetrics(final ClassMetrics classMetrics) {

		this.classMetricsRepository.saveClassMetrics(classMetrics);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateClassMetrics(final ClassMetrics classMetrics) {

		this.classMetricsRepository.updateClassMetrics(classMetrics);

	}



}
