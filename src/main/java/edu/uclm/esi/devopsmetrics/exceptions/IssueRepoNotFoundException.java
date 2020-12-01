package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class IssueRepoNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de issue repository no encontrado.
	   */
	  public IssueRepoNotFoundException(String id) {

	    super(String.format("Issue Repository with  Id '%s' not found", id));

	  }
}