package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class IssueCursorNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de issue cursor no encontrado.
	   */
	  public IssueCursorNotFoundException(String id) {

	    super(String.format("Issue Cursor with id '%s' not found", id));

	  }

}
