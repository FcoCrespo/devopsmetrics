package edu.uclm.esi.devopsmetrics.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.entities.TestMetrics;
import edu.uclm.esi.devopsmetrics.exceptions.TestMetricsNotFoundException;
import edu.uclm.esi.devopsmetrics.repositories.TestMetricsRepository;

@Service("TestMetricsService")
/**
 * @author FcoCrespo
 */
@Transactional
public class TestMetricsServiceImpl implements TestMetricsService{

	/**
	 * @author FcoCrespo
	 */
	private TestMetricsRepository testMetricsRepository;

	public TestMetricsServiceImpl(final TestMetricsRepository testMetricsRepository) {

		this.testMetricsRepository = testMetricsRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public TestMetrics findById(final String id) {

		final Optional<TestMetrics> testMetrics = this.testMetricsRepository.findOne(id);

		if (testMetrics.isPresent()) {

			final Optional<TestMetrics> testMetricsOpt = testMetrics;

			return testMetricsOpt.get();

		} else {

			throw new TestMetricsNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<TestMetrics> findAll() {

		final Optional<List<TestMetrics>> testMetrics = this.testMetricsRepository.findAll();

		final List<TestMetrics> testMetricsList = new ArrayList<TestMetrics>();

		if (testMetrics.isPresent()) {
			TestMetrics testMetric;
			for (int i = 0; i < testMetrics.get().size(); i++) {
				testMetric = testMetrics.get().get(i);
				testMetricsList.add(testMetric);
			}

			return testMetricsList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveTestMetrics(final TestMetrics testMetrics) {

		this.testMetricsRepository.saveTestMetrics(testMetrics);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateTestMetrics(final TestMetrics testMetrics) {

		this.testMetricsRepository.updateTestMetrics(testMetrics);

	}

	@Override
	public List<TestMetrics> getAllByRepositoryBeginEndDateByOwner(String repository, Instant beginDate,
			Instant endDate, String owner) {
		
		return this.testMetricsRepository.findAllByTestMetricsBeginEndDateByOwner(repository, beginDate, endDate, owner);

	}

	@Override
	public List<TestMetrics> getAllByRepositoryAndOwner(String repository, String owner) {
		
		return this.testMetricsRepository.findAllByTestMetricsAndOwner(repository, owner);

	}

}

