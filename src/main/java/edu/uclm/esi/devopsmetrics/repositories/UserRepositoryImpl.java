package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import edu.uclm.esi.devopsmetrics.models.User;

/**
 * Clase que implementa la interfaz UserRepository.
 * 
 * @author FcoCrespo
 */

@Repository
public class UserRepositoryImpl implements UserRepository {
	  /**
	   * Instancia de la interfaz MongoOperations.
	   * 
	   * @author FcoCrespo
	   */
	  private final MongoOperations mongoOperations;

	  /**
	   * Constructor de la clase.
	   * 
	   * @author FcoCrespo
	   */
	  @Autowired

	  public UserRepositoryImpl(final MongoOperations mongoOperations) {
	    Assert.notNull(mongoOperations, "notNull");
	    this.mongoOperations = mongoOperations;

	  }

	  /**
	   * Devuelve todos los usuarios.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<List<User>> findAll() {

	    List<User> users = this.mongoOperations.find(new Query(), User.class);

	    return Optional.ofNullable(users);

	  }

	  /**
	   * Devuelve un usuario en función de su username.
	   * 
	   * @author FcoCrespo
	   */
	  public Optional<User> findOne(final String username) {
	    User d = this.mongoOperations.findOne(new Query(Criteria.where("username").is(username)), User.class);
	    return Optional.ofNullable(d);
	  }

	  /**
	   * Guarda un usuario en la base de datos.
	   * 
	   * @author e3corp
	   */
	  public void saveUser(final User usuario) {
	    this.mongoOperations.save(usuario);
	  }

	  /**
	   * Actualiza un usuario en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void updateUser(final User usuario) {

	    this.mongoOperations.save(usuario);

	  }

	  /**
	   * Borra un usuario en la base de datos.
	   * 
	   * @author FcoCrespo
	   */
	  public void deleteUser(final String id) {

	    this.mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), User.class);

	  }

	  @Override
	  public User findByUsernameAndPassword(final String username, final String password) {
	    return this.mongoOperations
	        .findOne(new Query(Criteria.where("username").is(username).and("password").is(password)), User.class);
	  }

	  @Override
	  public List<User> findByRole(final String role) {
	    return this.mongoOperations.find(new Query(Criteria.where("role").is(role)), User.class);
	  }

	@Override
	public User findByTokenPass(String tokenpass) {
		return this.mongoOperations.findOne(new Query(Criteria.where("tokenPass").is(tokenpass)), User.class);

	}


	
}