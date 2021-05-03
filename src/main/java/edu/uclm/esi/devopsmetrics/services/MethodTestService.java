package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.MethodTest;

/**
 * @author FcoCrespo
 */
public interface MethodTestService {
	/**
	 * @author FcoCrespo
	 */
	List<MethodTest> findAll();

	/**
	 * @author FcoCrespo
	 */
	MethodTest findById(String id);

	/**
	 * @author FcoCrespo
	 */
	void saveMethodTest(MethodTest methodTest);

	/**
	 * @author FcoCrespo
	 */
	void updateMethodTest(MethodTest methodTest);
	
	/**
	 * @author FcoCrespo
	 */
	List<MethodTest> getAllByTestId(String idTest);


}