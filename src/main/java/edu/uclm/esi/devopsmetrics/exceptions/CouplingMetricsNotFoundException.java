package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class CouplingMetricsNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de coupling metrics no encontrado.
   */
  public CouplingMetricsNotFoundException(String id) {

    super(String.format("coupling metrics with id '%s' not found", id));

  }

}