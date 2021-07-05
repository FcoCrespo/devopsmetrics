package edu.uclm.esi.devopsmetrics.repositories;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.UserGithubRepos;

/**
 * Clase que implementa la interfaz UserGithubRepository.
 * 
 * @author FcoCrespo
 */

@Repository

public class UserGithubReposRepositoryImpl implements UserGithubReposRepository{
	
	/**
	 * Instancia de la interfaz MongoOperations.
	 * 
	 * @author FcoCrespo
	 */
	
	MongoOperations mongoOperations;
	private String ownerStr = "owner";
	private String repositoryStr = "repository";
	
	/**
	 * Constructor de la clase.
	 * 
	 * @author FcoCrespo
	 */
	@Autowired

	public UserGithubReposRepositoryImpl(MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations, "notNull");
		this.mongoOperations = mongoOperations;
		
	}

	@Override
	public Optional<List<UserGithubRepos>> findAll() {
		List<UserGithubRepos> userGithubrepos = this.mongoOperations.find(new Query(), UserGithubRepos.class);

		return Optional.ofNullable(userGithubrepos);
	}

	@Override
	public void saveUserGithubRepos(UserGithubRepos userGithubRepos) {
		this.mongoOperations.save(userGithubRepos);
		
	}

	@Override
	public void updateUserGithubRepos(UserGithubRepos userGithubRepos) {
		this.mongoOperations.save(userGithubRepos);
		
	}

	@Override
	public void deleteUserGithubRepos(String repository, String owner) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where(this.repositoryStr).is(repository).and(this.ownerStr).is(owner)), UserGithubRepos.class);
		
	}

	@Override
	public UserGithubRepos findByUserGithubReposData(String idusergithub, String repository, String owner) {
		return this.mongoOperations.findOne(new Query(Criteria.where("idusergithub").is(idusergithub)
				.and(this.repositoryStr).is(repository).and(this.ownerStr).is(owner)), UserGithubRepos.class);

	}

	@Override
	public List<UserGithubRepos> findByAllByRepoAndOwner(String repository, String owner) {
		return this.mongoOperations.find(new Query(Criteria.where(this.repositoryStr).is(repository).and(this.ownerStr).is(owner)), UserGithubRepos.class);

	}

}
