package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class MethodMetricsNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de method metrics no encontrado.
   */
  public MethodMetricsNotFoundException(String id) {

    super(String.format("method metrics with id '%s' not found", id));

  }

}