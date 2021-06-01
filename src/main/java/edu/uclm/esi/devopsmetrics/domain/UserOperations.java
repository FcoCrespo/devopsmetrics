package edu.uclm.esi.devopsmetrics.domain;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.uclm.esi.devopsmetrics.exceptions.UserNotFoundException;
import edu.uclm.esi.devopsmetrics.models.SecureUser;
import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

@Service
@Scope("singleton")
public class UserOperations {

	private UserService userService;
	private Utilities utilities;

	/**
	 * @author FcoCrespo
	 */
	private UserOperations() {
		
	}
	
	private static class UserOperationsHolder {
		static UserOperations singleton=new UserOperations();
	}
	
	public static UserOperations get() {
		return UserOperationsHolder.singleton;
	}

	public boolean getUserByUsernameAndPassword(String username, String password) {

		try {

			User usuariologin;
			usuariologin = userService.getUserByUsernameAndPassword(username, password);

			return usuariologin != null;

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			return false;
		}
	}

	public String sendSecureUser(String username, String password) {

		try {

			User usuariologin;
			usuariologin = userService.getUserByUsernameAndPassword(username, password);
			usuariologin.setUsernameUser(utilities.encriptar(username));
			usuariologin.setPasswordUser(utilities.encriptar(password));
			usuariologin.newTokenPass();
			usuariologin.setTokenValidity();

			userService.updateUser(usuariologin);

			SecureUser secureUser = new SecureUser(usuariologin.getIdUser(), username,
					utilities.desencriptar(usuariologin.getRoleUser()), usuariologin.getTokenPass(),
					usuariologin.getTokenValidity());

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(secureUser);

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| JsonProcessingException e) {
			return null;
		}

	}

	public boolean getUserByTokenPass(String tokenpass) {

		User usuariologin;
		usuariologin = userService.getUserByTokenPass(tokenpass);
		return usuariologin != null;

	}
	
	public boolean getUserByTokenPassAdmin(String tokenpass) {
		
		User usuariologin;
		usuariologin = userService.getUserByTokenPass(tokenpass);
		if(usuariologin != null) {
			try {
				return utilities.desencriptar(usuariologin.getRoleUser()).equals("admin");
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public String findByUsername(String username) {
		
		try {
			User user = userService.findByUsername(username);
			SecureUser secureUser = new SecureUser(user.getIdUser(), username, utilities.desencriptar(user.getRoleUser()),
					user.getTokenPass(), user.getTokenValidity());


			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(secureUser);

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| JsonProcessingException | UserNotFoundException e) {
			return null;
		}

	}

	public String getAllUsers() {
		
		try {
			
			List<User> users = userService.findAll();
			List<SecureUser> listaSecureUsers = new ArrayList<SecureUser>();
			SecureUser userSecure;
			for (int i = 0; i < users.size(); i++) {
				userSecure = new SecureUser(users.get(i).getIdUser(),
						utilities.desencriptar(users.get(i).getUsernameUser()), utilities.desencriptar(users.get(i).getRoleUser()),
						users.get(i).getTokenPass(), users.get(i).getTokenValidity());
				listaSecureUsers.add(userSecure);
			}
			
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			
			return ow.writeValueAsString(listaSecureUsers);
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| UserNotFoundException | JsonProcessingException e) {
			return null;
		}
		
	}

	public void deleteUser(String userId) {
		
		userService.deleteUser(userId);
		
	}

	public boolean getByUsername(String username) {
		User user;
		try {
			user = userService.findByUsername(username);
			return user!=null;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			return false;
		}
		
	}

	public void registrarUser(String username, String password, String role) {	
		try {
			User usuario = new User(utilities.encriptar(username), utilities.encriptar(password), utilities.encriptar(role));
			userService.saveUser(usuario);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.toString();
		}
	}

	public void actualizarUsuario(String username, String password, String role) {
		
		try {
			User usuario = userService.findByUsername(username);
			
			String usernameEncriptado = utilities.encriptar(username);
			String passwordEncriptado = utilities.encriptar(password);
			String roleEncriptado = utilities.encriptar(role);
			usuario.setUsernameUser(usernameEncriptado);
			usuario.setPasswordUser(passwordEncriptado);
			usuario.setRoleUser(roleEncriptado);
			
			userService.updateUser(usuario);
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.toString();
		}
		
		
	}

}
