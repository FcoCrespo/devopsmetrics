
package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.UserGithubRepos;

/**
 * Interfaz de UserGithubRepos.
 * 
 * @author FcoCrespo
 */
@Repository
public interface UserGithubReposRepository {

	/**
	   * Método que te devuelve todos los UserGithubRepos.
	   * 
	   * @author FcoCrespo
	   */
	  Optional<List<UserGithubRepos>> findAll();

	  /**
	   * Método para guardar UserGithubRepos.
	   * 
	   * @author FcoCrespo
	   */
	  void saveUserGithubRepos(UserGithubRepos userGithubRepos);

	  /**
	   * Método para actualizar UserGithubRepos.
	   * 
	   * @author FcoCrespo
	   */
	  void updateUserGithubRepos(UserGithubRepos userGithubRepos);

	  /**
	   * Método para borrar un UserGithubRepos por su id.
	   * 
	   * @author FcoCrespo
	   */
	  void deleteUserGithubRepos(String repository, String owner);

	  /**
	   * Método para obtener usergithubrepos por sus datos
	   * 
	   * @author FcoCrespo
	   */
	  UserGithubRepos findByUserGithubReposData(String idusergithub, String repository, String owner);
	  
	  /**
	   * Método para obtener usergithubrepos por sus datos de repositorio y owner
	   * 
	   * @author FcoCrespo
	   */
	  List<UserGithubRepos> findByAllByRepoAndOwner(String repository, String owner);
	  
	  /**
	   * Método para obtener usergithubrepos por sus datos de repositorio y owner
	   * 
	   * @author FcoCrespo
	   */
	  List<UserGithubRepos> findByByUserGithub(String idusergithub);
	  

}