package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.UserGithubRepos;

public interface UserGithubReposService {

	/**
	   * Método que te devuelve todos los UserGithubRepos.
	   * 
	   * @author FcoCrespo
	   */
	  List<UserGithubRepos> findAll();

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
	   * Método para obtener usergithubrepos por sus datos por repositorio y owner
	   * 
	   * @author FcoCrespo
	   */
	  List<UserGithubRepos> findAllByRepositoryAndOwner(String repository, String owner);
	  
	  /**
	   * Método para obtener los repositorios por su user github
	   * 
	   * @author FcoCrespo
	   */
	  List<UserGithubRepos> findAllByUserGithub(String idusergithub);
	  
}
