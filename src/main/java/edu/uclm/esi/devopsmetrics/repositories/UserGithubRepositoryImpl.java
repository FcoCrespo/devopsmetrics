package edu.uclm.esi.devopsmetrics.repositories;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.entities.UserGithub;

/**
 * Clase que implementa la interfaz UserGithubRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class UserGithubRepositoryImpl implements UserGithubRepository {

	/**
	 * Instancia de la interfaz MongoOperations.
	 * 
	 * @author FcoCrespo
	 */
	
	MongoOperations mongoOperations;
	
	/**
	 * Constructor de la clase.
	 * 
	 * @author FcoCrespo
	 */
	@Autowired

	public UserGithubRepositoryImpl(MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations, "notNull");
		this.mongoOperations = mongoOperations;
		
	}
	
	@Override
	public Optional<List<UserGithub>> findAll() {
		List<UserGithub> userGithub = this.mongoOperations.find(new Query(), UserGithub.class);

		return Optional.ofNullable(userGithub);
	}

	@Override
	public void saveUserGithub(UserGithub userGithub) {
		this.mongoOperations.save(userGithub);	
	}

	@Override
	public void updateUserGithub(UserGithub userGithub) {
		this.mongoOperations.save(userGithub);
	}

	@Override
	public void deleteUserGithub(String login) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where("login").is(login)), UserGithub.class);
	}

	@Override
	public UserGithub findById(String id) {
		return this.mongoOperations.findOne(new Query(Criteria.where("id").is(id)), UserGithub.class);
	}
	
	@Override
	public UserGithub findByLogin(String login) {
		return this.mongoOperations.findOne(new Query(Criteria.where("login").is(login)), UserGithub.class);
	}
	
	@Override
	public UserGithub findByName(String name) {
		return this.mongoOperations.findOne(new Query(Criteria.where("name").is(name)), UserGithub.class);
	}

}
