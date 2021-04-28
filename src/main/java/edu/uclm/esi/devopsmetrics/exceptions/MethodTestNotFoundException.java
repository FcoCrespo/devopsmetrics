package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class MethodTestNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de method test no encontrado.
   */
  public MethodTestNotFoundException(String id) {

    super(String.format("method test with id '%s' not found", id));

  }

}