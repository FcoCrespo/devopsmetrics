package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class IssueAssigneeNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de issue assignee no encontrado.
	   */
	  public IssueAssigneeNotFoundException(String id) {

	    super(String.format("Issue Assignee with  Id '%s' not found", id));

	  }
}