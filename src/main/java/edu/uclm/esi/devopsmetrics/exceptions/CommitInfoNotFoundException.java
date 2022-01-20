package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class CommitInfoNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de commitinfo no encontrado.
   */
  public CommitInfoNotFoundException(String id) {

    super(String.format("Commit with  id '%s' not found", id));

  }

}