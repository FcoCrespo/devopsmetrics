package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class UserGithubNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de usuario de github no encontrado.
	   */
	  public UserGithubNotFoundException(String id) {

	    super(String.format("UserGithub with  Id '%s' not found", id));

	  }
}