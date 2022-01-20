package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
*
* @author FcoCrespo
* 
*/
public class CohesionMetricsNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de cohesion metrics no encontrado.
   */
  public CohesionMetricsNotFoundException(String id) {

    super(String.format("cohesion metrics with id '%s' not found", id));

  }

}