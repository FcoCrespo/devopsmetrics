package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class IssueNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de issue no encontrado.
	   */
	  public IssueNotFoundException(String id) {

	    super(String.format("Issue with  Id '%s' not found", id));

	  }
}