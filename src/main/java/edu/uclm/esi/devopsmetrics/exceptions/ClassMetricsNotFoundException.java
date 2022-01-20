package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class ClassMetricsNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de class metrics no encontrado.
   */
  public ClassMetricsNotFoundException(String id) {

    super(String.format("class metrics with id '%s' not found", id));

  }

}