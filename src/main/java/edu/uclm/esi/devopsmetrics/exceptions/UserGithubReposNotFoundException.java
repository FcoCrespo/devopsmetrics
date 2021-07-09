package edu.uclm.esi.devopsmetrics.exceptions;

import org.springframework.core.NestedRuntimeException;

public class UserGithubReposNotFoundException extends NestedRuntimeException {

	  private static final long serialVersionUID = 1L;

	  /**
	   * Este método muestra el mensaje de excepción de user github github repos no encontrado.
	   */
	  public UserGithubReposNotFoundException(String data1, String data2, String data3) {

	    super(String.format("user github repos with referencen data: '%s' , '%s' , '%s' not found", data1, data2, data3));

	  }

	}