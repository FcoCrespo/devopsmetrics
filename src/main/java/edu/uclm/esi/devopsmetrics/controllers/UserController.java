package edu.uclm.esi.devopsmetrics.controllers;


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
 * @author FcoCrespo
 * "https://esidevopsmetrics.herokuapp.com"
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
public class UserController {

	private static final Log LOG = LogFactory.getLog(UserController.class);

	private String errorMesage;

	@Autowired
	/**
	 * @author FcoCrespo
	 */
	public UserController() {
		this.errorMesage = "No se ha encontrado ningún usuario con esos datos.";
	}

	/**
	 * Comprueba que el usuario existe por su username y su pass.
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping
	public ResponseEntity <String> getUserPassword(@RequestParam("username") final String username,
			@RequestParam("password") final String password){

		final boolean existe = UserOperations.get().getUserByUsernameAndPassword(username, password);
		
		if (existe) {
			LOG.info("Usuario encontrado");
			return ResponseEntity.ok(UserOperations.get().sendSecureUser(username, password));
		} else {
			LOG.info(this.errorMesage);
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

		final boolean existe = UserOperations.get().getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Buscando usuario: " + username);
			return ResponseEntity.ok(UserOperations.get().findByUsername(username));
		} else {
			LOG.info(this.errorMesage);
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

		final boolean existe = UserOperations.get().getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get all Users");
			return ResponseEntity.ok(UserOperations.get().getAllUsers());
		} else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body("El usuario no tiene iniciada sus sesión.");
		}

	}
	
	/**
	 * Obtiene un usuario por su username
	 * 
	 * @author FcoCrespo
	 */
	@GetMapping(value = "/getuser")
	@ApiOperation(value = "Find an user by his username", notes = "Find an user by his username")

	public ResponseEntity <String> getUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("username") final String username){

		final boolean existe = UserOperations.get().getUserByTokenPass(tokenpass);
		if (existe) {
			LOG.info("Get user");
			return ResponseEntity.ok(UserOperations.get().findByUsername(username));
		} else {
			LOG.info(this.errorMesage);
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

		final boolean existe = UserOperations.get().getUserByTokenPassAdmin(tokenpass);
		if (existe) {
			LOG.info("Delete user " + userId);
			UserOperations.get().deleteUser(userId);
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
	 * 
	 */
	@PostMapping
	public ResponseEntity <String> registrarUsuario(@RequestBody final String usuario, 
			@RequestParam("tokenpass") final String tokenpass) {

		final JSONObject jso = new JSONObject(usuario);
		final String username = jso.getString("username");
		final String password = jso.getString("password");

		final boolean tokenpassCorrect = UserOperations.get().getUserByTokenPassAdmin(tokenpass);

		if(tokenpassCorrect) {
			boolean existe = UserOperations.get().getByUsername(username);
			if (!(existe)) {
				LOG.info("Registrando usuario...");
				
				String role = jso.getString("role");
				
				UserOperations.get().registrarUser(username, password, role);
				
				LOG.info("Usuario registrado.");
				return ResponseEntity.ok("Usuario registrado correctamente.");
				
			} else {
				LOG.info("Error: El usuario ya está registrado.");
				return ResponseEntity.badRequest().body("Error: El usuario ya está registrado.");
			}
		}
		else {
			LOG.info(this.errorMesage);
			return ResponseEntity.badRequest().body(this.errorMesage);
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

		final boolean existe = UserOperations.get().getUserByTokenPass(tokenpass);
		if (existe) {
			final JSONObject jso = new JSONObject(mensajerecibido);
			boolean existeUsername = UserOperations.get().getByUsername(username);
			if (!(existeUsername)) {
				LOG.info("Error: El usuario no existe.");
				return ResponseEntity.badRequest().body("El usuario no existe.");
			} else {
				try {
					LOG.info("Actualizando usuario...");
					
					final String password = jso.getString("password");
					final String role = jso.getString("role");
					
					UserOperations.get().actualizarUsuario(username, password, role);
					
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
	
}
