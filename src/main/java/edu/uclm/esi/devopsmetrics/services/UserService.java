package edu.uclm.esi.devopsmetrics.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
   */
  User findByUsername(String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException;

 
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
  void deleteUser(String username);

  /**
   * @author FcoCrespo
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
   */
  User getUserByUsernameAndPassword(String username, String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException;
  
  /**
   * @author FcoCrespo
   */
  User getUserByTokenPass(String tokenPass);

  /**
   * @author e3corp
   */
  List<User> getUsersByRole(String role);

}