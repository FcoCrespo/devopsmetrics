package edu.uclm.esi.devopsmetrics.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;

@Service
@Scope("singleton")
public class UserGithubOperations {
	
	private UserGithubService userGithubService;

	/**
	 * @author FcoCrespo
	 */
	private UserGithubOperations() {
		
	}
	
	private static class UserGithubOperationsHolder {
		static UserGithubOperations singleton=new UserGithubOperations();
	}
	
	public static UserGithubOperations get() {
		return UserGithubOperationsHolder.singleton;
	}

	public UserGithub saveAuthor(String [] authorValues) {
		
		String authorLogin = authorValues[0];
		String authorName  = authorValues[1];
		String authorEmail = authorValues[3];
		String authorAvatarURL = authorValues[4];
		
		UserGithub userGithub;
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		
		if( authorLogin != null && !authorLogin.equals("")) {
			userGithub = userGithubService.findByLogin(authorLogin);
			
			if(userGithub==null) {
				userGithub = new UserGithub(authorLogin, authorEmail, authorAvatarURL, uuid, authorName);
				userGithubService.saveUserGithub(userGithub);
			}
		
		}
		
		else if( authorName != null && !authorName.equals("")) {
			userGithub = userGithubService.findByName(authorName);
			
			if(userGithub==null) {
				userGithub = new UserGithub(authorLogin, authorEmail, authorAvatarURL, uuid, authorName);
				userGithubService.saveUserGithub(userGithub);
			}
		
		}
		
		userGithub = userGithubService.findByLogin(authorLogin);
		if(userGithub==null) {
			userGithub = userGithubService.findByName(authorName);
		}
		
		return userGithub;
		
	}

	public UserGithub findById(String id) {
		return userGithubService.findById(id);
	}

	public List<UserGithub> findAll() {
		return userGithubService.findAll();
	}

	public UserGithub findByName(String authorName) {
		return userGithubService.findByName(authorName);
	}
}
