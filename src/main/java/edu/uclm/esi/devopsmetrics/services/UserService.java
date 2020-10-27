package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.User;

/**
 * @author FcoCrespo
 */
public interface UserService {
  /**
   * @author FcoCrespo
   */
  List<User> findAll();

  /**
   * @author FcoCrespo
   */
  User findByUsername(String username);

  /**
   * @author FcoCrespo
   */
  void saveUser(User user);

  /**
   * @author FcoCrespo
   */
  void updateUser(User user);

  /**
   * @author FcoCrespo
   */
  void deleteUser(String userId);

  /**
   * @author FcoCrespo
   */
  User getUserByUsernameAndPassword(String username, String password);
  
  /**
   * @author FcoCrespo
   */
  User getUserByTokenPass(String tokenPass);

  /**
   * @author e3corp
   */
  List<User> getUsersByRole(String role);

}