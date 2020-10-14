package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class CommitNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de commit no encontrado.
   */
  public CommitNotFoundException(String oid) {

    super(String.format("Commit with  oid '%s' not found", oid));

  }

}