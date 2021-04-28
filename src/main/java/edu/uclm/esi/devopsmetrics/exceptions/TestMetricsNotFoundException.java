package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class TestMetricsNotFoundException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Este método muestra el mensaje de excepción de test metrics no encontrado.
   */
  public TestMetricsNotFoundException(String id) {

    super(String.format("test metrics with id '%s' not found", id));

  }

}