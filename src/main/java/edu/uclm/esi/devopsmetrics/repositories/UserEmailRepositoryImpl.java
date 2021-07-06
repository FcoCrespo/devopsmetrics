package edu.uclm.esi.devopsmetrics.repositories;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.UserEmail;

/**
 * Clase que implementa la interfaz UserGithubRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class UserEmailRepositoryImpl implements UserEmailRepository{

	/**
	 * Instancia de la interfaz MongoOperations.
	 * 
	 * @author FcoCrespo
	 */
	
	MongoOperations mongoOperations;
	private String usernameStr;
	/**
	 * Constructor de la clase.
	 * 
	 * @author FcoCrespo
	 */
	@Autowired
	
	public UserEmailRepositoryImpl(MongoOperations mongoOperations) {
		Assert.notNull(mongoOperations, "notNull");
		this.mongoOperations = mongoOperations;
		this.usernameStr = "username";
	}
	
	@Override
	public Optional<List<UserEmail>> findAll() {
		List<UserEmail> userEmail = this.mongoOperations.find(new Query(), UserEmail.class);

		return Optional.ofNullable(userEmail);
	}

	@Override
	public void saveUserEmail(UserEmail userEmail) {
		this.mongoOperations.save(userEmail);
		
	}

	@Override
	public void updateUserEmail(UserEmail userEmail) {
		this.mongoOperations.save(userEmail);
	}

	@Override
	public void deleteUserEmail(String username) {
		this.mongoOperations.findAllAndRemove(new Query(Criteria.where(this.usernameStr).is(username)), UserEmail.class);

		
	}

	@Override
	public UserEmail findByUsernameAndEmail(String username, String email) {
		return this.mongoOperations.findOne(new Query(Criteria.where(this.usernameStr).is(username).and("email").is(email)), UserEmail.class);
	}

	@Override
	public UserEmail findByUsername(String username) {
		return this.mongoOperations.findOne(new Query(Criteria.where(this.usernameStr).is(username)), UserEmail.class);

	}

}
