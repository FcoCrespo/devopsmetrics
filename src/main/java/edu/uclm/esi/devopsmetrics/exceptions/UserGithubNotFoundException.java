package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class UserGithubNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de user github no encontrado.
	   */
	  public UserGithubNotFoundException(String id) {

	    super(String.format("user github with referencen data: '%s' ,  not found", id));

	  }

	}