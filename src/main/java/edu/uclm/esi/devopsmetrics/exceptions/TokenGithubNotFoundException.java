package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class TokenGithubNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de token gihub no encontrado.
	   */
	  public TokenGithubNotFoundException(String id) {

	    super(String.format("token github with owner '%s' not found", id));

	  }

	}
