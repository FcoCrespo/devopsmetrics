package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class BranchNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de commit no encontrado.
   */
  public BranchNotFoundException(String id) {

    super(String.format("Branch with id '%s' not found", id));

  }

}