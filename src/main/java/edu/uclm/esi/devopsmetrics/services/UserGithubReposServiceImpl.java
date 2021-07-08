package edu.uclm.esi.devopsmetrics.services;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import edu.uclm.esi.devopsmetrics.models.UserGithubRepos;
import edu.uclm.esi.devopsmetrics.repositories.UserGithubReposRepository;

@Service("UserGithubReposService")
/**
 * @author FcoCrespo
 */
@Transactional

public class UserGithubReposServiceImpl implements UserGithubReposService{
	
	/**
	   * @author FcoCrespo
	   */
	  private UserGithubReposRepository userGithubReposRepository;

	  /**
	   * @author FcoCrespo
	   */
	  @Autowired

	  public UserGithubReposServiceImpl(final UserGithubReposRepository userGithubReposRepository) {

	    this.userGithubReposRepository = userGithubReposRepository;

	  }

	@Override
	public List<UserGithubRepos> findAll() {
		final Optional<List<UserGithubRepos>> usersGithubRepos = userGithubReposRepository.findAll();

	    final List<UserGithubRepos> usersGithubReposList = new ArrayList<UserGithubRepos>();
	    
	    if(usersGithubRepos.isPresent()) {
	    	
	    	UserGithubRepos userGithubrepos;
	    	for (int i = 0; i < usersGithubRepos.get().size(); i++) {
	    		userGithubrepos = usersGithubRepos.get().get(i);
	    		usersGithubReposList.add(userGithubrepos);
	        }

	        return usersGithubReposList;
	    }
	    else {
	    	return Collections.emptyList();
	    }
	}

	@Override
	public void saveUserGithubRepos(UserGithubRepos userGithubRepos) {
		this.userGithubReposRepository.saveUserGithubRepos(userGithubRepos);
		
	}

	@Override
	public void updateUserGithubRepos(UserGithubRepos userGithubRepos) {
		this.userGithubReposRepository.saveUserGithubRepos(userGithubRepos);
		
	}

	@Override
	public void deleteUserGithubRepos(String repository, String owner) {
		this.userGithubReposRepository.deleteUserGithubRepos(repository, owner);
		
	}

	@Override
	public UserGithubRepos findByUserGithubReposData(String idusergithub, String repository, String owner) {
		return this.userGithubReposRepository.findByUserGithubReposData(idusergithub, repository, owner);
	}

	@Override
	public List<UserGithubRepos> findAllByRepositoryAndOwner(String repository, String owner) {
		return this.userGithubReposRepository.findByAllByRepoAndOwner(repository, owner);

	}

	@Override
	public List<UserGithubRepos> findAllByUserGithub(String idusergithub) {
		return this.userGithubReposRepository.findByByUserGithub(idusergithub);
	}

}
