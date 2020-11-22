package edu.uclm.esi.devopsmetrics.services;


import java.util.List;

import edu.uclm.esi.devopsmetrics.models.UserGithub;

/**
 * @author FcoCrespo
 */
public interface UserGithubService {

	/**
	   * @author FcoCrespo
	   */
	  List<UserGithub> findAll();

	  /**
	   * @author FcoCrespo
	   */
	  UserGithub findById(String id);
	  
	  /**
	   * @author FcoCrespo
	   */
	  UserGithub findByLogin(String login);
	  
	  /**
	   * @author FcoCrespo
	   */
	  UserGithub findByName(String name);

	  /**
	   * @author FcoCrespo
	   */
	  void saveUserGithub(UserGithub userGithub);

	  /**
	   * @author FcoCrespo
	   */
	  void updateUserGithub(UserGithub userGithub);

	  /**
	   * @author FcoCrespo
	   */
	  void deleteUserGithub(String login);
}
