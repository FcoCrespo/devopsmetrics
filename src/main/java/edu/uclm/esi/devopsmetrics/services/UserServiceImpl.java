package edu.uclm.esi.devopsmetrics.services;


import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
  private Utilities utilities;

  /**
   * @author FcoCrespo
   */
  @Autowired

  public UserServiceImpl(final UserRepository userRepository, final Utilities utilities) {

    this.userRepository = userRepository;
    this.utilities = utilities;
  }

  /**
   * @author FcoCrespo
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
   */
  public User findByUsername(final String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

	  List <User> usuarios = findAll();
		
		String userdesencriptado;
		
		User user = null;
		
		boolean seguir = true;
		for(int i = 0; i<usuarios.size()&&seguir; i++) {
			userdesencriptado = this.utilities.desencriptar(usuarios.get(i).getUsernameUser());
			if(username.equals(userdesencriptado)) {
				user = usuarios.get(i);
				seguir=false;
			}
		}
	    if(user!=null) {
	    	return user;
	    }
	    else {
	    	return null;
	    }

  }

  /**
   * @author FcoCrespo
   */
  public List<User> findAll() {

    final Optional<List<User>> users = userRepository.findAll();

    final List<User> listaUsuarios = new ArrayList<User>();
    
    User usuario;

    if(users.isPresent()) {
    	for (int i = 0; i < users.get().size(); i++) {
            usuario = users.get().get(i);
            listaUsuarios.add(usuario);
       }

       return listaUsuarios;
    }
    else {
    	return Collections.emptyList();
    }

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
  public User getUserByUsernameAndPassword(final String username, final String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

	List <User> usuarios = findAll();
	
	String userdesencriptado;
	String passworddesencriptado;
	
	User user = null;
	
	boolean seguir = true;
	for(int i = 0; i<usuarios.size()&&seguir; i++) {
               
		userdesencriptado = this.utilities.desencriptar(usuarios.get(i).getUsernameUser());
		passworddesencriptado = this.utilities.desencriptar(usuarios.get(i).getPasswordUser());
		if(username.equals(userdesencriptado) && password.equals(passworddesencriptado)) {
			user = usuarios.get(i);
			seguir=false;
		}
		
	}
    if(user!=null) {
    	return user;
    }
    else {
    	return null;
    }
    
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