package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.UserEmail;

/**
 * Interfaz de UserEmailRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface UserEmailRepository {

	/**
	   * Método que te devuelve todos los usuarios con email.
	   * 
	   * @author FcoCrespo
	   */
	  Optional<List<UserEmail>> findAll();

	  /**
	   * Método para guardar un usuario con email.
	   * 
	   * @author FcoCrespo
	   */
	  void saveUserEmail(UserEmail userEmail);

	  /**
	   * Método para actualizar un usuario con email.
	   * 
	   * @author FcoCrespo
	   */
	  void updateUserEmail(UserEmail userEmail);

	  /**
	   * Método para borrar un usaurio con email por su username.
	   * 
	   * @author FcoCrespo
	   */
	  void deleteUserEmail(String username);

	  /**
	   * Método para obtener un usuario con email por su username y email
	   * 
	   * @author FcoCrespo
	   */
	  UserEmail findByUsernameAndEmail(String username, String email);
	  
	  /**
	   * Método para obtener un usuario con email por su username
	   * 
	   * @author FcoCrespo
	   */
	  UserEmail findByUsername(String username);
	  
	  
}
