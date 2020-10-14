package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class CommitCursorNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de commit no encontrado.
   */
  public CommitCursorNotFoundException(String endCursor) {

    super(String.format("Commit with  end cursor '%s' not found", endCursor));

  }

}