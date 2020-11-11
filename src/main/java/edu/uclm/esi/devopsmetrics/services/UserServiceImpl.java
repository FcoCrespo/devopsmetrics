package edu.uclm.esi.devopsmetrics.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.UserNotFoundException;
import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.repositories.UserRepository;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

@Service("UserService")
/**
 * @author FcoCrespo
 */
@Transactional

public class UserServiceImpl implements UserService {
  
  /**
   * @author FcoCrespo
   */
  private UserRepository userRepository;

  /**
   * @author FcoCrespo
   */
  @Autowired

  public UserServiceImpl(final UserRepository userRepository) {

    this.userRepository = userRepository;

  }

  /**
   * @author FcoCrespo
   */
  public User findByUsername(final String username) {

    final Optional<User> user = userRepository.findOne(username);

    if (user.isPresent()) {

      final Optional<User> userDesencriptado = Utilities.desencriptarOptionalUser(user);

      if(userDesencriptado.isPresent()){
    	  return userDesencriptado.get();
      }
      else {
    	  return null;
      }

    } else {

      throw new UserNotFoundException(username);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<User> findAll() {

    final Optional<List<User>> users = userRepository.findAll();

    return Utilities.desencriptarListaUsers(users);

  }

  /**
   * @author FcoCrespo
   */
  public void saveUser(final User user) {

    userRepository.saveUser(user);

  }

  /**
   * @author FcoCrespo
   */
  public void updateUser(final User user) {

    userRepository.updateUser(user);

  }

  /**
   * @author FcoCrespo
   */
  public void deleteUser(final String userId) {

    userRepository.deleteUser(userId);

  }

  @Override
  public User getUserByUsernameAndPassword(final String username, final String password) {

    final User user = userRepository.findByUsernameAndPassword(username, password);
    return Utilities.desencriptarUser(user);
    
  }

  @Override
  public List<User> getUsersByRole(final String role) {
    return userRepository.findByRole(role);
  }

	@Override
	public User getUserByTokenPass(String tokenPass) {
		return userRepository.findByTokenPass(tokenPass);
	}



}