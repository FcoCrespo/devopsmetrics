package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.User;

/**
 * Interfaz de UserRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface UserRepository {
  /**
   * Método que te devuelve todos los usuarios.
   * 
   * @author FcoCrespo
   */
  Optional<List<User>> findAll();

  /**
   * Método para guardar un usuario.
   * 
   * @author FcoCrespo
   */
  void saveUser(User user);

  /**
   * Método para actualizar un usuario.
   * 
   * @author FcoCrespo
   */
  void updateUser(User user);

  /**
   * Método para borrar un usuario.
   * 
   * @author FcoCrespo
   */
  void deleteUser(String userId);

  /**
   * Método para obtener un usuario por su username.
   * 
   * @author FcoCrespo
   */
  Optional<User> findOne(String username);

  /**
   * Método para obtener un usuario por su nombre de usuario y contraseña.
   * 
   * @author FcoCrespo
   */
  User findByUsernameAndPassword(String username, String password);

  /**
   * Método para obtener un usuario en función de su rol.
   * 
   * @author FcoCrespo
   */
  List<User> findByRole(String role);

}