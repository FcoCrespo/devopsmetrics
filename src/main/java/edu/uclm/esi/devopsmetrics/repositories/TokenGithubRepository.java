package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.TokenGithub;

/**
 * Interfaz de UserRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface TokenGithubRepository {
  /**
   * Método que te devuelve tokenGithub.
   * 
   * @author FcoCrespo
   */
  List<TokenGithub> findAll();

  /**
   * Método para guardar un TokenGithub.
   * 
   * @author FcoCrespo
   */
  void saveTokenGithub(TokenGithub tokenGithub);

  /**
   * Método para actualizar un TokenGithub.
   * 
   * @author FcoCrespo
   */
  void updateTokenGithub(TokenGithub tokenGithub);

  /**
   * Método para borrar un TokenGithub.
   * 
   * @author FcoCrespo
   */
  void deleteTokenGithub(String tokenGithub);

  /**
   * Método para obtener TokenGithub por su owner
   * 
   * @author FcoCrespo
   */
  TokenGithub findByOwner(String owner);

  /**
   * Método para obtener un usuario por su TokenGithub y owner
   * 
   * @author FcoCrespo
   */
  TokenGithub findByOwnerAndToken(String owner, String tokenGithub);
 
}