package edu.uclm.esi.devopsmetrics.controllers;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;

@RestController
@RequestMapping("/usuarios")
/**
 * @author FcoCrespo "https://webesidevopsmetrics.herokuapp.com"
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*")
public class UserController {

	private static final Log LOG = LogFactory.getLog(UserController.class);

	private String errorMesage;
	private UserOperations userOperations;

	@Autowired
	/**
	 * @author FcoCrespo
	 */
	public UserController(UserOperations userOperations) {
		this.errorMesage = "No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
	}

	/**
	 * Comprueba que el usuario existe por su username y su pass.
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@GetMapping
	public ResponseEntity<String> getUserPassword(@RequestParam("username") final String username,
			@RequestParam("password") final String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByUsernameAndPassword(username, password);

		if (existe) {
			LOG.info("Usuario encontrado");
			return ResponseEntity.ok(this.userOperations.sendSecureUser(username, password));
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * Obtiene los usuarios mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@GetMapping(value = "/all")
	@ApiOperation(value = "Find all user", notes = "Return all users")

	public ResponseEntity<String> allUsers(@RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get all Users");
			return ResponseEntity.ok(this.userOperations.getAllUsers());
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario no esta iniciado.");
		}

	}
	
	/**
	 * Obtiene los usuarios mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@GetMapping(value = "/usersgithubfree")
	@ApiOperation(value = "Find all user", notes = "Return all users")

	public ResponseEntity<String> getUsersGithubFree(@RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get all Users Github Free");
			return ResponseEntity.ok(this.userOperations.getAllUsersGithubFree());
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario no tiene iniciada sus sesión.");
		}

	}

	/**
	 * Obtiene un usuario por su username
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@GetMapping(value = "/getuser")
	@ApiOperation(value = "Find an user by his username", notes = "Find an user by his username")

	public ResponseEntity<String> getUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("username") final String username) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get user");
			return ResponseEntity.ok(this.userOperations.findByUsername(username));
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario no tiene iniciada sus sesión.");
		}

	}

	/**
	 * Borra un usuario en funcion de su username mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	@DeleteMapping(value = "/deleteuser")
	@ApiOperation(value = "Delete an user", notes = "Delete a user by username")

	public ResponseEntity<String> deleteUser(@RequestParam("username") final String username,
			@RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByTokenPassAdmin(tokenpass);
		if (existe) {
			LOG.info("Delete user " + username);
			this.userOperations.deleteUser(username);
			return ResponseEntity.ok("Usuario eliminado correctamente.");
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body(this.errorMesage);
		}

	}

	/**
	 * Registramos un usuario y guardamos ese usuario en la base de datos.
	 * 
	 * @author FcoCrespo
	 * @throws MessagingException 
	 * @throws AddressException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * 
	 */
	@PostMapping
	public ResponseEntity<String> registrarUsuario(@RequestBody final String message,
			@RequestParam("tokenpass") final String tokenpass) throws MessagingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final JSONObject jso = new JSONObject(message);
		

		final boolean tokenpassCorrect = this.userOperations.getUserByTokenPassAdmin(tokenpass);

		if (tokenpassCorrect) {
			final String username = jso.getString("username");

			
	
			String user = this.userOperations.findByUsername(username);
			
			if (user==null) {
				LOG.info("Registrando usuario...");

				final String email = jso.getString("email");
				final String role = jso.getString("role");
				final String userGithub = jso.getString("userGithub");

				this.userOperations.registrarUser(username, role, email, userGithub);

				LOG.info("Usuario registrado.");
				return ResponseEntity.ok("Usuario registrado correctamente.");

			} else {
				LOG.info("Error: El usuario ya está registrado.");
				return ResponseEntity.badRequest().body("Error: El usuario ya está registrado.");
			}
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body(this.errorMesage);
		}

	}

	/**
	 * Modificamos los campos de un usuario y guardamos los cambios en la base de
	 * datos.
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * 
	 */
	@PutMapping(value = "/{username}")
	@ApiOperation(value = "Update usuario", notes = "Finds a username and updates its fields")
	public ResponseEntity<String> updateUsuario(@RequestBody final String mensajerecibido,
			@PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(mensajerecibido);
			
			JSONObject json = new JSONObject(this.userOperations.findByUsername(username));
			String id = json.getString("id");
			if (id==null) {
				LOG.info("Error: El usuario no existe.");
				return ResponseEntity.badRequest().body("El usuario no existe.");
			} else {
				try {
					LOG.info("Actualizando usuario...");

					final String password = jso.getString("password");

					this.userOperations.actualizarUsuario(username, password);

					LOG.info("[SERVER] Usuario actualizado.");
					return ResponseEntity.ok("Usuario actualizado correctamente.");

				} catch (JSONException e) {
					LOG.error("Error en la lectura del elemento recibido.");
					LOG.info(e.getMessage());
					return ResponseEntity.badRequest().body("Error en la lectura del elemento recibido");
				}

			}
		} else {
			LOG.info("No se ha encontrado ningún usuario con esos datos para actualizar.");
			return ResponseEntity.badRequest().body("El usuario no existe.");
		}

	}
	
	/**
	 * Operacion para recuperar la contraseña por correo a partir del usuario y el email
	 * 
	 * @author FcoCrespo
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws MessagingException 
	 */
	@GetMapping(value = "/recoverpassword")
	@ApiOperation(value = "Find all user", notes = "Return all users")

	public ResponseEntity<String> recoverPassword(@RequestParam("username") final String username,
			@RequestParam("email") final String email) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, MessagingException {

		final boolean existe = this.userOperations.getByUsernameAndEmail(username, email);
		if (existe) {
			LOG.info("Recover password");
			this.userOperations.recoverPassword(username, email);
			return ResponseEntity.ok("Password recovered.");
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body("No existe un usuario con ese usuario y email en el sistema.");
		}

	}
	
	

}
