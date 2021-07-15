package edu.uclm.esi.devopsmetrics.services;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.uclm.esi.devopsmetrics.entities.UserEmail;

public interface UserEmailService {

	/**
	   * Método que te devuelve todos los UserEmail.
	   * 
	   * @author FcoCrespo
	   */
	  List<UserEmail> findAll();

	  /**
	   * Método para guardar UserEmail.
	   * 
	   * @author FcoCrespo
	   */
	  void saveUserEmail(UserEmail userEmail);

	  /**
	   * Método para actualizar UserEmail.
	   * 
	   * @author FcoCrespo
	   */
	  void updateUserEmail(UserEmail userEmail);

	  /**
	   * Método para borrar un UserEmail por su username
	   * 
	   * @author FcoCrespo
	   */
	  void deleteUserGithubRepos(String username);

	  /**
	   * Método para obtener UserEmail por sus datos
	   * 
	   * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	   */
	  UserEmail findByUsernameAndEmail(String username, String email) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException;
	  
	  /**
	   * Método para obtener UserEmail por su username
	   * 
	   * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	   */
	  UserEmail findByUsername(String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException;
	  
	  
}
