package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class IssueRepoNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de issue repo no encontrado.
	   */
	  public IssueRepoNotFoundException(String id) {

	    super(String.format("Issue Repo with id '%s' not found", id));

	  }

}