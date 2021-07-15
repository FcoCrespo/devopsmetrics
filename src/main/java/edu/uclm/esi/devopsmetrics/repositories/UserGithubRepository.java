package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.UserGithub;

/**
 * Interfaz de UserGithubRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface UserGithubRepository {

	/**
	   * Método que te devuelve todos los usuarios de github.
	   * 
	   * @author FcoCrespo
	   */
	  Optional<List<UserGithub>> findAll();

	  /**
	   * Método para guardar un usuario de github.
	   * 
	   * @author FcoCrespo
	   */
	  void saveUserGithub(UserGithub userGithub);

	  /**
	   * Método para actualizar un usuario de github.
	   * 
	   * @author FcoCrespo
	   */
	  void updateUserGithub(UserGithub userGithub);

	  /**
	   * Método para borrar un usaurio de github por su login.
	   * 
	   * @author FcoCrespo
	   */
	  void deleteUserGithub(String login);

	  /**
	   * Método para obtener un usuario de Github por el campo login del mismo.
	   * 
	   * @author FcoCrespo
	   */
	  UserGithub findByLogin(String login);
	  
	  /**
	   * Método para obtener un usuario de Github por el campo id del mismo.
	   * 
	   * @author FcoCrespo
	   */
	  UserGithub findById(String id);
	  
	  
	  /**
	   * Método para obtener un usuario de Github por el campo name del mismo.
	   * 
	   * @author FcoCrespo
	   */
	  UserGithub findByName(String name);

}
