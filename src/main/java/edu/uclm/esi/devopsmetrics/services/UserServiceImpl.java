package edu.uclm.esi.devopsmetrics.services;


import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
  private static final Log log = LogFactory.getLog(UserServiceImpl.class);
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

      log.debug(String.format("Read username '{}'", username));

      final Optional<User> userDesencriptado = Utilities.desencriptarOptionalUser(user);

      return userDesencriptado.get();

    } else {

      throw new UserNotFoundException(username);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<User> findAll() {

    final Optional<List<User>> users = userRepository.findAll();

    final List<User> usersDesencrip = Utilities.desencriptarListaUsers(users);

    return usersDesencrip;

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
    final User usuarioDesencriptado = Utilities.desencriptarUser(user);
    return usuarioDesencriptado;
    
  }

  @Override
  public List<User> getUsersByRole(final String role) {
    final List<User> usersRole = userRepository.findByRole(role);
    return usersRole;
  }

	@Override
	public User getUserByTokenPass(String tokenPass) {
		final User user = userRepository.findByTokenPass(tokenPass);
	    final User usuarioDesencriptado = Utilities.desencriptarUser(user);
	    return usuarioDesencriptado;
	}



}