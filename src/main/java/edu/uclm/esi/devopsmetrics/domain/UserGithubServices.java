package edu.uclm.esi.devopsmetrics.domain;

import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.services.UserEmailService;
import edu.uclm.esi.devopsmetrics.services.UserGithubReposService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;
import edu.uclm.esi.devopsmetrics.services.UserService;

/**
*
* @author FcoCrespo
* 
*/
@Service
public class UserGithubServices {

	private final UserGithubService userGithubService;
	private final UserGithubReposService userGithubReposService;
	private final UserService userService;
	private final UserEmailService userEmailService;
	


	/**
	 * @author FcoCrespo
	 */
	public UserGithubServices(UserGithubService userGithubService,
			UserGithubReposService userGithubReposService,
			UserService userService,
			UserEmailService userEmailService) {

		this.userGithubService = userGithubService;
		this.userGithubReposService = userGithubReposService;
		this.userService = userService;
		this.userEmailService =  userEmailService;
	
	}



	public UserGithubService getUserGithubService() {
		return userGithubService;
	}



	public UserGithubReposService getUserGithubReposService() {
		return userGithubReposService;
	}



	public UserService getUserService() {
		return userService;
	}



	public UserEmailService getUserEmailService() {
		return userEmailService;
	}


	

	
}
