package edu.uclm.esi.devopsmetrics.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.exceptions.UserNotFoundException;
import edu.uclm.esi.devopsmetrics.models.User;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;

@RestController
@RequestMapping("/usuarios")
/**
 * @author FcoCrespo
 */
//src CORS: https://www.arquitecturajava.com/spring-rest-cors-y-su-configuracion/
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*")
public class UserController {

  private static final Log LOG = LogFactory.getLog(UserController.class);

  private final UserService usersService;

  @Autowired
  /**
   * @author FcoCrespo
   */
  public UserController(final UserService usersService) {
    this.usersService = usersService;
  }

  /**
   * Obtiene la contraseña del usuario mediante su dni.
   * 
   * @author FcoCrespo
   */

  @RequestMapping(method = RequestMethod.GET)

  public ResponseEntity<User> getUserPassword(@RequestParam("username") final String username,
      @RequestParam("password") final String password) {

    final String usernameEncriptado = Utilities.encriptar(username);
    final String contrasenaEncrip = Utilities.encriptar(password);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameEncriptado, contrasenaEncrip);
    if (usuariologin != null) {
      LOG.info("[SERVER] Usuario encontrado: " + usuariologin.getUsername());
      return ResponseEntity.ok(usuariologin);
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Obtiene un usuario mediante su username.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.GET)
  @ApiOperation(value = "Find an user", notes = "Return a user by username")

  public ResponseEntity<User> userByUsername(@PathVariable final String username, @RequestParam("username") final String usernamelogin,
	      @RequestParam("password") final String passwordlogin) throws UserNotFoundException {
	
    final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
    if (usuariologin != null) {
    	LOG.info("[SERVER] Buscando usuario: " + username);
        User user;
        try {
          final String usernameEncriptado = Utilities.encriptar(username);
          user = usersService.findByUsername(usernameEncriptado);
          LOG.info("[SERVER] Usuario encontrado.");
        } catch (UserNotFoundException e) {
          user = null;
          LOG.error("[SERVER] Usuario no encontrado.");
        }
        return ResponseEntity.ok(user);
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    } 
  }

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @ApiOperation(value = "Find all user", notes = "Return all users")

  public ResponseEntity<List<User>> allUsers(@RequestParam("username") final String usernamelogin,
	      @RequestParam("password") final String passwordlogin) {
	
	final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

    final User usuario = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
    if (usuario != null) {
      LOG.info("Get allUsers");
      return ResponseEntity.ok(usersService.findAll());
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
	
  }

  /**
   * Borra un usuario en funcion de su id.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Delete an user", notes = "Delete a user by Id")

  public ResponseEntity<Void> deleteUser(@PathVariable final String userId, @RequestParam("username") final String usernamelogin,
	      @RequestParam("password") final String passwordlogin) {
	
	final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
    if (usuariologin != null) {
      LOG.info("Delete user " + userId);
      usersService.deleteUser(userId);
      return ResponseEntity.noContent().build();
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }

  }

  /**
   * Registramos un usuario y guardamos ese usuario en la base de datos.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(method = RequestMethod.POST)

  public ResponseEntity<User> registrarUsuario(@RequestBody final String usuario, @RequestParam("username") final String usernamelogin,
	      @RequestParam("password") final String passwordlogin) {
	  
	final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
    if (usuariologin != null) {
    	final JSONObject jso = new JSONObject(usuario);
        final String username = jso.getString("username");
        final String password = jso.getString("password");

        final String usernameEncriptado = Utilities.encriptar(username);

        final String passwordEncrip = Utilities.encriptar(password);

        User usuario1 = usersService.getUserByUsernameAndPassword(usernameEncriptado, passwordEncrip);
        if (usuario1 == null) {
          String role = null;
          try {
            LOG.info("[SERVER] Registrando usuario...");
            role = jso.getString("role");
          } catch (JSONException j) {
            LOG.info("[SERVER] Error en la lectura del JSON.");
            LOG.info(j.getMessage());
            return ResponseEntity.badRequest().build();
          }

          usuario1 = new User(username, password, role);
          usersService.saveUser(usuario1);
          LOG.info("[SERVER] Usuario registrado.");
          LOG.info("[SERVER] " + usuario1.toString());
          return ResponseEntity.ok().build();
        } else {
          LOG.info("[SERVER] Error: El usuario ya está registrado.");
          LOG.info("[SERVER] " + usuario1.toString());
          return ResponseEntity.badRequest().build();
        }
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }   
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
  @ApiOperation(value = "Update usuario", notes = "Finds a username and updates its fields")
  public ResponseEntity<User> updateUsuario(@RequestBody final String mensajerecibido,
      @PathVariable final String username, @RequestParam("username") final String usernamelogin,
      @RequestParam("password") final String passwordlogin) {
    
	final String usernameloginEncriptado = Utilities.encriptar(usernamelogin);
    final String contrasenaloginEncriptado = Utilities.encriptar(passwordlogin);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameloginEncriptado, contrasenaloginEncriptado);
    if (usuariologin != null) {
    	final JSONObject jso = new JSONObject(mensajerecibido);
        final String usernameEncriptado = Utilities.encriptar(username);
        final User usuario = usersService.findByUsername(usernameEncriptado);
        if (usuario == null) {
          LOG.info("[SERVER] Error: el usuario no existe.");
          return ResponseEntity.badRequest().build();
        } else {
          try {
            LOG.info("[SERVER] Actualizando usuario...");

            // Depende de los campos que queramos que puedan actualizarse
            final String role = jso.getString("role");
            final String password = jso.getString("password");

            
            
            final String passwordEncriptado = Utilities.encriptar(password);
            final String roleEncriptado = Utilities.encriptar(role);
            usuario.setUsername(usernameEncriptado);
            usuario.setRole(roleEncriptado);
            usuario.setPassword(passwordEncriptado);
          } catch (JSONException j) {
            LOG.error("[SERVER] Error en la lectura del JSON.");
            LOG.info(j.getMessage());
            return ResponseEntity.badRequest().build();
          }

          usersService.updateUser(usuario);
          LOG.info("[SERVER] Usuario actualizada.");
          LOG.info("[SERVER] " + usuario.toString());
          return ResponseEntity.ok().build();
        }
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
  }

}