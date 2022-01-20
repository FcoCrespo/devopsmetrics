package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class BranchNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de branch no encontrado.
   */
  public BranchNotFoundException(String id) {

    super(String.format("Branch with id '%s' not found", id));

  }

}