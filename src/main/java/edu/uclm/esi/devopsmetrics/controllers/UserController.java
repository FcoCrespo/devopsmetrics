package edu.uclm.esi.devopsmetrics.controllers;

import java.util.ArrayList;
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
import edu.uclm.esi.devopsmetrics.models.SecureUser;
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
  
  
  @RequestMapping(value = "/hola", method = RequestMethod.GET)

  public String getHola() throws Exception {
	  return "hola amigo";
  }

  /**
   * Comprueba que el usuario existe por su username y su pass.
   * 
   * @author FcoCrespo
   * @throws Exception 
   */

  @RequestMapping(method = RequestMethod.GET)

  public ResponseEntity <SecureUser> getUserPassword(@RequestParam("username") final String username,
      @RequestParam("password") final String password) throws Exception {

    final String usernameEncriptado = Utilities.encriptar(username);
    final String contrasenaEncrip = Utilities.encriptar(password);

    final User usuariologin = usersService.getUserByUsernameAndPassword(usernameEncriptado, contrasenaEncrip);
    if (usuariologin != null) {
      LOG.info("[SERVER] Usuario encontrado: " + usuariologin.getUsername());
      usuariologin.setUsername(Utilities.encriptar(usuariologin.getUsername()));
      usuariologin.setRole(Utilities.encriptar(usuariologin.getRole()));
      usuariologin.newTokenPass();
      usuariologin.setTokenValidity();
      
      usersService.updateUser(usuariologin);
      
      usuariologin.setUsername(Utilities.desencriptar(usuariologin.getUsername()));
      usuariologin.setRole(Utilities.desencriptar(usuariologin.getRole()));
      
      SecureUser secureUser = new SecureUser(usuariologin.getId(), usuariologin.getUsername(), usuariologin.getRole(), usuariologin.getTokenPass(), usuariologin.getTokenValidity());
      
      return ResponseEntity.ok(secureUser);
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
  }
  
  /**
   * Obtiene un usuario mediante el token de acceso.
   * 
   * @author FcoCrespo
   * @throws UserNotFoundException
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.GET)
  @ApiOperation(value = "Find an user", notes = "Return a user by username")

  public ResponseEntity <SecureUser> userByTokenPass(@PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) throws UserNotFoundException {

    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
    if (usuariologin != null) {
    	LOG.info("[SERVER] Buscando usuario: " + username);
        User user;
        try {
          final String usernameEncriptado = Utilities.encriptar(username);
          user = usersService.findByUsername(usernameEncriptado);
          LOG.info("[SERVER] Usuario encontrado.");
          
          SecureUser secureUser = new SecureUser(user.getId(), user.getUsername(), user.getRole(), user.getTokenPass(), user.getTokenValidity());
          return ResponseEntity.ok(secureUser);
        } catch (UserNotFoundException e) {
          user = null;
          LOG.error("[SERVER] Usuario no encontrado.");
          return ResponseEntity.badRequest().build();
        }
        
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    } 
  }
  
  /**
   * Obtiene los usuarios mediante el token de acceso.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @ApiOperation(value = "Find all user", notes = "Return all users")
  
  public ResponseEntity<List<SecureUser>> allUsers(@RequestParam("tokenpass") final String tokenpass) {
	
    final User usuario = usersService.getUserByTokenPass(tokenpass);
    if (usuario != null) {
      LOG.info("Get allUsers");
      List <User> users = usersService.findAll();
      List<SecureUser> listaSecureUsers=new ArrayList<SecureUser>();
      SecureUser userSecure;
      for(int i=0; i<users.size(); i++) {
    	  userSecure = new SecureUser(users.get(i).getId(), users.get(i).getUsername(),users.get(i).getRole(), users.get(i).getTokenPass(), users.get(i).getTokenValidity() ); 
    	  listaSecureUsers.add(userSecure);
      }
      return ResponseEntity.ok(listaSecureUsers);
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
	
  }

  /**
   * Borra un usuario en funcion de su id mediante el token de acceso.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Delete an user", notes = "Delete a user by Id")

  public ResponseEntity<Void> deleteUser(@PathVariable final String userId, @RequestParam("tokenpass") final String tokenpass) {

    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
    if (usuariologin != null && usuariologin.getRole().equals("admin")) {
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
  public ResponseEntity<User> registrarUsuario(@RequestBody final String usuario) {
	  
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

          usuario1 = new User(Utilities.encriptar(username), Utilities.encriptar(password), Utilities.encriptar(role));
          usersService.saveUser(usuario1);
          LOG.info("[SERVER] Usuario registrado.");
          LOG.info("[SERVER] " + usuario1.toString());
          return ResponseEntity.ok().build();
        } else {
          LOG.info("[SERVER] Error: El usuario ya está registrado.");
          LOG.info("[SERVER] " + usuario1.toString());
          return ResponseEntity.badRequest().build();
        }
      
  }

  /**
   * Modificamos los campos de un usuario y guardamos los cambios en la base de datos.
   * 
   * @author FcoCrespo
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
  @ApiOperation(value = "Update usuario", notes = "Finds a username and updates its fields")
  public ResponseEntity<User> updateUsuario(@RequestBody final String mensajerecibido,
      @PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) {

    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
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

          
            final String role = jso.getString("role");
            final String password = jso.getString("password");
            final String passwordEncriptado = Utilities.encriptar(password);
            final String roleEncriptado = Utilities.encriptar(role);
            usuario.setUsername(usernameEncriptado);
            usuario.setRole(roleEncriptado);
            usuario.setPassword(passwordEncriptado);
            usersService.updateUser(usuario);
            LOG.info("[SERVER] Usuario actualizada.");
            LOG.info("[SERVER] " + usuario.toString());
            return ResponseEntity.ok().build();
            
          } catch (JSONException j) {
            LOG.error("[SERVER] Error en la lectura del JSON.");
            LOG.info(j.getMessage());
            return ResponseEntity.badRequest().build();
          }

        }
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    }
  }
  
}