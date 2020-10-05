package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class UserNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de usuario no encontrado.
   */
  public UserNotFoundException(String id) {

    super(String.format("User with  Id '%s' not found", id));

  }

}