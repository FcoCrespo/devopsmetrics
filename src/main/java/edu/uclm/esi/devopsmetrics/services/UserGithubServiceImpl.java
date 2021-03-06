package edu.uclm.esi.devopsmetrics.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.entities.UserGithub;
import edu.uclm.esi.devopsmetrics.exceptions.UserGithubNotFoundException;
import edu.uclm.esi.devopsmetrics.repositories.UserGithubRepository;

@Service("UserGithubService")
/**
 * @author FcoCrespo
 */
@Transactional

public class UserGithubServiceImpl implements UserGithubService{
	
	/**
	   * @author FcoCrespo
	   */
	  private UserGithubRepository userGithubRepository;

	  /**
	   * @author FcoCrespo
	   */
	  @Autowired

	  public UserGithubServiceImpl(final UserGithubRepository userGithubRepository) {

	    this.userGithubRepository = userGithubRepository;

	  }

	@Override
	public List<UserGithub> findAll() {
		final Optional<List<UserGithub>> usersGithub = userGithubRepository.findAll();

	    final List<UserGithub> usersGithubList = new ArrayList<UserGithub>();
	    
	    if(usersGithub.isPresent()) {
	    	
	    	UserGithub userGithub;
	    	for (int i = 0; i < usersGithub.get().size(); i++) {
	    		userGithub = usersGithub.get().get(i);
	    		usersGithubList.add(userGithub);
	        }

	        return usersGithubList;
	    }
	    else {
	    	return Collections.emptyList();
	    }
	}

	@Override
	public UserGithub findById(String id) {
		final UserGithub userGithub = userGithubRepository.findById(id);
		if(userGithub!=null) {
			return userGithub;
	    } else {
	    	throw new UserGithubNotFoundException(id);
	    }
	}
	
	@Override
	public UserGithub findByLogin(String login) {
		final UserGithub userGithub = userGithubRepository.findByLogin(login);
		if(userGithub!=null) {
			return userGithub;
	    } else {
	    	throw new UserGithubNotFoundException(login);
	    }
	}
	
	
	@Override
	public UserGithub findByName(String name) {
		final UserGithub userGithub = userGithubRepository.findByName(name);
		if(userGithub!=null) {
			return userGithub;
	    } else {
	    	throw new UserGithubNotFoundException(name);
	    }
	}

	@Override
	public void saveUserGithub(UserGithub userGithub) {
		this.userGithubRepository.saveUserGithub(userGithub);
		
	}

	@Override
	public void updateUserGithub(UserGithub userGithub) {
		this.userGithubRepository.updateUserGithub(userGithub);
		
	}

	@Override
	public void deleteUserGithub(String login) {
		this.userGithubRepository.deleteUserGithub(login);
		
	}

}
