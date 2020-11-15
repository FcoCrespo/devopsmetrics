package edu.uclm.esi.devopsmetrics.controllers;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.uclm.esi.devopsmetrics.domain.UserOperations;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/usuarios")
/**
 * @author FcoCrespo
 */

@CrossOrigin(origins = { "http://localhost:4200", "https://esidevopsmetrics.herokuapp.com" }, allowedHeaders = "*")
@Configuration
@EnableWebSecurity(debug = false) 
public class UserController {

	private Logger logger = Logger.getLogger(UserController.class.getName());

	private String errorMesage;
	private UserOperations userOperations;

	@Autowired
	/**
	 * @author FcoCrespo
	 */
	public UserController(UserOperations userOperations) {
		this.errorMesage = "No se ha encontrado ningún usuario con esos datos.";
		this.userOperations = userOperations;
		this.logger = Logger.getLogger(UserController.class.getName());
	}

	/**
	 * Comprueba que el usuario existe por su username y su pass.
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping
	public ResponseEntity <String> getUserPassword(@RequestParam("username") final String username,
			@RequestParam("password") final String password){

		final boolean existe = this.userOperations.getUserByUsernameAndPassword(username, password);
		
		if (existe) {
			logger.log(Level.INFO, "Usuario encontrado");
			return ResponseEntity.ok(this.userOperations.sendSecureUser(username, password));
		} else {
			logger.log(Level.INFO, this.errorMesage);
			return ResponseEntity.badRequest().build();
		}
		
	}
	

	/**
	 * Obtiene un usuario mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/{username}")
	@ApiOperation(value = "Find an user", notes = "Return a user by username")

	public ResponseEntity <String> userByTokenPass(@PathVariable final String username,
			@RequestParam("tokenpass") final String tokenpass){

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			logger.log(Level.INFO,"Buscando usuario: " + username);
			return ResponseEntity.ok(this.userOperations.findByUsername(username));
		} else {
			logger.log(Level.INFO,this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario, contraseña o ambos campos son incorrectos.");
		}
	}
	
	/**
	 * Obtiene los usuarios mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/all")
	@ApiOperation(value = "Find all user", notes = "Return all users")

	public ResponseEntity <String> allUsers(@RequestParam("tokenpass") final String tokenpass){

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			logger.log(Level.INFO,"Get all Users");
			return ResponseEntity.ok(this.userOperations.getAllUsers());
		} else {
			logger.log(Level.INFO,this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario no tiene iniciada sus sesión.");
		}

	}
	
	/**
	 * Borra un usuario en funcion de su id mediante el token de acceso.
	 * 
	 * @author FcoCrespo
	 */
	@DeleteMapping(value = "/{userId}")
	@ApiOperation(value = "Delete an user", notes = "Delete a user by Id")

	public ResponseEntity <String> deleteUser(@PathVariable final String userId,
			@RequestParam("tokenpass") final String tokenpass) {

		final boolean existe = this.userOperations.getUserByTokenPassAdmin(tokenpass);
		if (existe) {
			logger.log(Level.INFO,"Delete user " + userId);
			this.userOperations.deleteUser(userId);
			return ResponseEntity.ok("Usuario eliminado correctamente.");
		} else {
			logger.log(Level.INFO,this.errorMesage);
			return ResponseEntity.badRequest().body(this.errorMesage);
		}

	}
	
	/**
	 * Registramos un usuario y guardamos ese usuario en la base de datos.
	 * 
	 * @author FcoCrespo
	 * 
	 */
	@PostMapping
	public ResponseEntity <String> registrarUsuario(@RequestBody final String usuario) {

		final JSONObject jso = new JSONObject(usuario);
		final String username = jso.getString("username");
		final String password = jso.getString("password");


		boolean existe = this.userOperations.getByUsername(username);
		if (!(existe)) {
			logger.log(Level.INFO,"Registrando usuario...");
			
			String role = jso.getString("role");
			
			this.userOperations.registrarUser(username, password, role);
			
			logger.log(Level.INFO,"Usuario registrado.");
			return ResponseEntity.ok("Usuario registrado correctamente.");
			
		} else {
			logger.log(Level.INFO,"Error: El usuario ya está registrado.");
			return ResponseEntity.badRequest().body("Error: El usuario ya está registrado.");
		}

	}
	
	/**
	 * Modificamos los campos de un usuario y guardamos los cambios en la base de
	 * datos.
	 * 
	 * @author FcoCrespo
	 * 
	 */
	@PutMapping(value = "/{username}")
	@ApiOperation(value = "Update usuario", notes = "Finds a username and updates its fields")
	public ResponseEntity <String> updateUsuario(@RequestBody final String mensajerecibido,
			@PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) {

		final boolean existe = this.userOperations.getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(mensajerecibido);
			boolean existeUsername = this.userOperations.getByUsername(username);
			if (!(existeUsername)) {
				logger.log(Level.INFO,"Error: El usuario no existe.");
				return ResponseEntity.badRequest().body("El usuario no existe.");
			} else {
				try {
					logger.log(Level.INFO,"Actualizando usuario...");
					
					final String password = jso.getString("password");
					final String role = jso.getString("role");
					
					this.userOperations.actualizarUsuario(username, password, role);
					
					logger.log(Level.INFO,"[SERVER] Usuario actualizado.");
					return ResponseEntity.ok("Usuario actualizado correctamente.");

				} catch (JSONException e) {
					logger.log(Level.INFO,"Error en la lectura del elemento recibido.");
					logger.log(Level.INFO,e.getMessage());
					return ResponseEntity.badRequest().body("Error en la lectura del elemento recibido");
				}

			}
		} else {
			logger.log(Level.INFO,"No se ha encontrado ningún usuario con esos datos para actualizar.");
			return ResponseEntity.badRequest().body("El usuario no existe.");
		}
	}
	
}
