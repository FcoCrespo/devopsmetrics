package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class UserNotFoundException  extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de user no encontrado.
	   */
	  public UserNotFoundException(String username) {

	    super(String.format("user  with referencen data: '%s' ,  not found", username));

	  }

	}