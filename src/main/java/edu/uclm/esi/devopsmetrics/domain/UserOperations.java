package edu.uclm.esi.devopsmetrics.domain;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.models.UserEmail;
import edu.uclm.esi.devopsmetrics.services.UserEmailService;
import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;

@Service
public class UserOperations {

	private static final Log LOG = LogFactory.getLog(UserOperations.class);
	
	@Value("${system.email}")
	private String emailsystem;
	@Value("${system.emailpass}")
	private String emailsystempass;
	
	private final UserService userService;
	private final UserEmailService userEmailService;
	private final Utilities utilities;
	
	private final String idStr;
	private final String usernameStr;
	private final String tokenPassStr;
	private final String userGithubStr;
	private final String roleStr;
	private final String emailStr;

	/**
	 * @author FcoCrespo
	 */
	public UserOperations(final UserService userService, final UserEmailService userEmailService, final Utilities utilities) {
		this.userService = userService;
		this.userEmailService =  userEmailService;
		this.utilities = utilities;
		this.idStr = "id";
		this.usernameStr = "username";
		this.tokenPassStr = "tokenPass";
		this.userGithubStr = "userGithub";
		this.roleStr = "role";
		this.emailStr = "email";
	}

	public boolean getUserByUsernameAndPassword(String username, String password) {

		try {

			User usuariologin;
			usuariologin = this.userService.getUserByUsernameAndPassword(username, password);

			return usuariologin != null;

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			return false;
		}
	}

	public String sendSecureUser(String username, String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {


			User usuariologin;
			usuariologin = this.userService.getUserByUsernameAndPassword(username, password);
			usuariologin.setUsernameUser(this.utilities.encriptar(username));
			usuariologin.setPasswordUser(this.utilities.encriptar(password));
			usuariologin.newTokenPass();
			
			UserEmail usuarioEmail = this.userEmailService.findByUsername(username);
			String email = this.utilities.desencriptar(usuarioEmail.getEmail());
			usuarioEmail.setUsername(this.utilities.encriptar(username));
			usuarioEmail.setEmail(this.utilities.encriptar(email));
			
			this.userService.updateUser(usuariologin);
			this.userEmailService.updateUserEmail(usuarioEmail);
			
			JSONObject secureUser = new JSONObject();
			secureUser.put("id", usuariologin.getIdUser());
			secureUser.put(this.usernameStr, username);
			secureUser.put("role", this.utilities.desencriptar(usuariologin.getRoleUser()));
			secureUser.put(this.tokenPassStr, usuariologin.getTokenPass());
			secureUser.put(this.userGithubStr, usuariologin.getUserGithub());
			secureUser.put(this.emailStr, this.utilities.desencriptar(usuarioEmail.getEmail()));

			return secureUser.toString();


	}

	public boolean getUserByTokenPass(String tokenpass) {

		User usuariologin;
		usuariologin = this.userService.getUserByTokenPass(tokenpass);
		return usuariologin != null;

	}

	public boolean getUserByTokenPassAdmin(String tokenpass) {

		User usuariologin;
		usuariologin = this.userService.getUserByTokenPass(tokenpass);
		if (usuariologin != null) {
			try {
				return this.utilities.desencriptar(usuariologin.getRoleUser()).equals("admin");
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public String findByUsername(String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

	     	User user = this.userService.findByUsername(username);
			UserEmail userEmail = this.userEmailService.findByUsername(username);
			
			JSONObject secureUser = new JSONObject();
			
			if(user == null) {
				return null;
			}
			secureUser.put(this.idStr, user.getIdUser());
			secureUser.put(this.usernameStr, username);
			secureUser.put(this.roleStr, this.utilities.desencriptar(user.getRoleUser()));
			secureUser.put(this.tokenPassStr, user.getTokenPass());
			secureUser.put(this.userGithubStr, user.getUserGithub());
			secureUser.put(this.emailStr, this.utilities.desencriptar(userEmail.getEmail()));

			return secureUser.toString();

		
	}
	
	public String comprobarCampo(String campo) {
		if(campo==null) {
			return null;
		}
		else {
			return campo;
		}
	}

	public String getAllUsers() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		

			List<User> users = this.userService.findAll();
			
			JSONArray array = new JSONArray();
			JSONObject secureUser;
			for (int i = 0; i < users.size(); i++) {
				secureUser = new JSONObject();
				secureUser.put(this.idStr, users.get(i).getIdUser());
				String username = this.utilities.desencriptar(users.get(i).getUsernameUser());
				secureUser.put(this.usernameStr, username);
				secureUser.put(this.roleStr, this.utilities.desencriptar(users.get(i).getRoleUser()));
				secureUser.put(this.tokenPassStr, users.get(i).getTokenPass());
				secureUser.put(this.userGithubStr, users.get(i).getUserGithub());
				UserEmail userMail= this.userEmailService.findByUsername(username);
				secureUser.put(this.emailStr, this.utilities.desencriptar(userMail.getEmail()));
				
				array.put(secureUser);
			}

			return array.toString();
	}

	public void deleteUser(String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		UserEmail userEmail = this.userEmailService.findByUsername(username);
		User user = this.userService.findByUsername(username);
		this.userEmailService.deleteUserGithubRepos(userEmail.getUsername());
		this.userService.deleteUser(user.getUsernameUser());

	}

	public boolean getByUsernameAndEmail(String username, String email) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		UserEmail useremail;
		
		useremail = this.userEmailService.findByUsernameAndEmail(username, email);
		return useremail != null;
		
	}

	public void registrarUser(String username, String role, String email, String userGithub) throws MessagingException {
		try {
			UUID uuid = UUID. randomUUID();
			String uuidAsString = uuid. toString();
			String temporalPassword = uuidAsString.substring(0, 12);
			User usuario = new User(this.utilities.encriptar(username), this.utilities.encriptar(temporalPassword),
					this.utilities.encriptar(role));
			this.userService.saveUser(usuario);
			UserEmail userEmail = new UserEmail(this.utilities.encriptar(username), this.utilities.encriptar(email));
			this.userEmailService.saveUserEmail(userEmail);
			if(userGithub!=null) {
				usuario.setUserGithub(userGithub);
			}
			
			sendEmail(username, temporalPassword, role, email);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.toString();
		}
	}

	

	public void actualizarUsuario(String username, String password, String role, String email) {

		try {
			User usuario = this.userService.findByUsername(username);
			UserEmail userEmail =  this.userEmailService.findByUsername(username);

			String usernameEncriptado = this.utilities.encriptar(username);
			String passwordEncriptado = this.utilities.encriptar(password);
			String roleEncriptado = this.utilities.encriptar(role);
			String emailEncriptado = this.utilities.encriptar(email);
			usuario.setUsernameUser(usernameEncriptado);
			usuario.setPasswordUser(passwordEncriptado);
			usuario.setRoleUser(roleEncriptado);
			userEmail.setEmail(emailEncriptado);
			
			this.userService.updateUser(usuario);
			this.userEmailService.updateUserEmail(userEmail);

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.toString();
		}

	}

	public void recoverPassword(String username, String email) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, MessagingException {
		
		UUID uuid = UUID. randomUUID();
		String uuidAsString = uuid. toString();
		String temporalPassword = uuidAsString.substring(0, 12);
		User usuario = this.userService.findByUsername(username);
		usuario.setPasswordUser(this.utilities.encriptar(temporalPassword));
		this.userService.updateUser(usuario);
		String role = this.utilities.desencriptar(usuario.getRoleUser());
		
		sendEmail(username, temporalPassword, role, email);
	}

	
	private void sendEmail(String username, String temporalPassword, String role, String email) throws MessagingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); 
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
        			@Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailsystem, emailsystempass);
                    }
                });
        
     
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailsystem));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(email)
        );
        message.setSubject("Data access for new user");
        message.setText("Hello, \n\n"
        		+ "your user data to access ESIDEVOPSMETRICS are:\n\n"
        		+ "user: "+username+"\n"
        		+ "password: "+temporalPassword+"\n"
        		+ "role: "+role+"\n\n"
        		+"Remember that you can change your password if you want in User Ops menu.\n\n"
        		+"Thank you.\n\n"
        		+"Kind regards. \n\n"
        		+ "https://webesidevopsmetrics.herokuapp.com");

        Transport.send(message);

        String mensaje ="\nEmail enviado al correo "+email;
        LOG.info(mensaje);
		
	}
}
