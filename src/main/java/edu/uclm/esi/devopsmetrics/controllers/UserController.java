package edu.uclm.esi.devopsmetrics.controllers;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
  
  
  @GetMapping(value = "/hola")

  public String getHola(){
	  return "hola amigo";
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
  public ResponseEntity <SecureUser> getUserPassword(@RequestParam("username") final String username,
      @RequestParam("password") final String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    final User usuariologin = usersService.getUserByUsernameAndPassword(username, password);
    if (usuariologin != null) {
      LOG.info("[SERVER] Usuario encontrado: " + usuariologin.getUsername());
      usuariologin.setUsername(Utilities.encriptar(username));
      usuariologin.setPassword(Utilities.encriptar(password));
      usuariologin.newTokenPass();
      usuariologin.setTokenValidity();
      
      usersService.updateUser(usuariologin);
      
      SecureUser secureUser = new SecureUser(usuariologin.getId(), username, usuariologin.getRole(), usuariologin.getTokenPass(), usuariologin.getTokenValidity());
      
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
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
 * @throws InvalidAlgorithmParameterException 
   * @throws UserNotFoundException
   */
  @GetMapping(value = "/{username}")
  @ApiOperation(value = "Find an user", notes = "Return a user by username")

  public ResponseEntity <SecureUser> userByTokenPass(@PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
    if (usuariologin != null) {
    	LOG.info("[SERVER] Buscando usuario: " + username);
        User user;
        try {
     
          user = usersService.findByUsername(username);
          LOG.info("[SERVER] Usuario encontrado.");
          
          SecureUser secureUser = new SecureUser(user.getId(), username, user.getRole(), user.getTokenPass(), user.getTokenValidity());
          return ResponseEntity.ok(secureUser);
        } catch (UserNotFoundException e) {
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
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
   */
  @GetMapping(value = "/all")
  @ApiOperation(value = "Find all user", notes = "Return all users")
  
  public ResponseEntity<List<SecureUser>> allUsers(@RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	
    final User usuario = usersService.getUserByTokenPass(tokenpass);
    if (usuario != null) {
      LOG.info("Get allUsers");
      List <User> users = usersService.findAll();
      List<SecureUser> listaSecureUsers=new ArrayList<SecureUser>();
      SecureUser userSecure;
      for(int i=0; i<users.size(); i++) {
    	  userSecure = new SecureUser(users.get(i).getId(), Utilities.desencriptar(users.get(i).getUsername()),users.get(i).getRole(), users.get(i).getTokenPass(), users.get(i).getTokenValidity() ); 
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
  @DeleteMapping(value = "/{userId}")
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
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
  
   */
  @PostMapping
  public ResponseEntity<User> registrarUsuario(@RequestBody final String usuario) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	  
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
          LOG.info("[SERVER] answer register user: " + usuario1.toString());
          return ResponseEntity.ok().build();
        } else {
          LOG.info("[SERVER] Error: El usuario ya está registrado.");
          LOG.info("[SERVER] answer register user fail:" + usuario1.toString());
          return ResponseEntity.badRequest().build();
        }
      
  }

  /**
   * Modificamos los campos de un usuario y guardamos los cambios en la base de datos.
   * 
   * @author FcoCrespo
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws InvalidAlgorithmParameterException 
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 

   */
  @PutMapping(value = "/{username}")
  @ApiOperation(value = "Update usuario", notes = "Finds a username and updates its fields")
  public ResponseEntity<User> updateUsuario(@RequestBody final String mensajerecibido,
      @PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    final User usuariologin = usersService.getUserByTokenPass(tokenpass);
    if (usuariologin != null) {
    	final JSONObject jso = new JSONObject(mensajerecibido);
        final User usuario = usersService.findByUsername(username);
        if (usuario == null) {
          LOG.info("[SERVER] Error: el usuario no existe.");
          return ResponseEntity.badRequest().build();
        } else {
          try {
            LOG.info("[SERVER] Actualizando usuario...");

          
            final String role = jso.getString("role");
            final String password = jso.getString("password");
            final String usernameEncriptado = Utilities.encriptar(username);
            final String passwordEncriptado = Utilities.encriptar(password);
            usuario.setUsername(usernameEncriptado);
            usuario.setRole(role);
            usuario.setPassword(passwordEncriptado);
            usersService.updateUser(usuario);
            LOG.info("[SERVER] Usuario actualizado.");
            return ResponseEntity.ok().build();
            
          } catch (JSONException j) {
            LOG.error("[SERVER] Error en la lectura del JSON.");
            LOG.info(j.getMessage());
            return ResponseEntity.badRequest().build();
          }

        }
    } else {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos para actualizar.");
      return ResponseEntity.badRequest().build();
    }
  }
  
}
