package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class UserEmailNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de user email no encontrado.
	   */
	  public UserEmailNotFoundException(String user, String email) {

	    super(String.format("user email with email '%s'  and user '%s' not found", email, user));

	  }

	}