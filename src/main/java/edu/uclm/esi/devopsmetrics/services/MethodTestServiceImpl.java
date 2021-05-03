package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.MethodTestNotFoundException;
import edu.uclm.esi.devopsmetrics.models.MethodTest;
import edu.uclm.esi.devopsmetrics.repositories.MethodTestRepository;

@Service("MethodTestService")
/**
 * @author FcoCrespo
 */
@Transactional
public class MethodTestServiceImpl implements MethodTestService{

	/**
	 * @author FcoCrespo
	 */
	private MethodTestRepository methodTestRepository;

	public MethodTestServiceImpl(final MethodTestRepository methodTestRepository) {

		this.methodTestRepository = methodTestRepository;

	}

	/**
	 * @author FcoCrespo
	 */
	public MethodTest findById(final String id) {

		final Optional<MethodTest> methodTests = this.methodTestRepository.findOne(id);

		if (methodTests.isPresent()) {

			final Optional<MethodTest> methodTestsOpt = methodTests;

			return methodTestsOpt.get();

		} else {

			throw new MethodTestNotFoundException(id);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<MethodTest> findAll() {

		final Optional<List<MethodTest>> methodTests = this.methodTestRepository.findAll();

		final List<MethodTest> methodTestList = new ArrayList<MethodTest>();

		if (methodTests.isPresent()) {
			MethodTest methodTest;
			for (int i = 0; i < methodTests.get().size(); i++) {
				methodTest = methodTests.get().get(i);
				methodTestList.add(methodTest);
			}

			return methodTestList;
		} else {
			return Collections.emptyList();
		}

	}

	/**
	 * @author FcoCrespo
	 */
	public void saveMethodTest(final MethodTest methodTest) {

		this.methodTestRepository.saveMethodTest(methodTest);

	}

	/**
	 * @author FcoCrespo
	 */
	public void updateMethodTest(final MethodTest methodTest) {

		this.methodTestRepository.updateMethodTest(methodTest);

	}


	@Override
	public List<MethodTest> getAllByTestId(String idTest) {
		
		return this.methodTestRepository.findAllByAllByTestId(idTest);

	}

}

