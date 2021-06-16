package edu.uclm.esi.devopsmetrics.domain;

import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.services.UserGithubReposService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;

@Service
public class UserGithubServices {

	private final UserGithubService userGithubService;
	private final UserGithubReposService userGithubReposService;


	/**
	 * @author FcoCrespo
	 */
	public UserGithubServices(UserGithubService userGithubService,
			UserGithubReposService userGithubReposService) {

		this.userGithubService = userGithubService;
		this.userGithubReposService = userGithubReposService;
	
	}


	public UserGithubService getUserGithubService() {
		return userGithubService;
	}


	public UserGithubReposService getUserGithubReposService() {
		return userGithubReposService;
	}

	
}
