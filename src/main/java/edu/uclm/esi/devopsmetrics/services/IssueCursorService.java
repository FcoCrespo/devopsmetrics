package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.IssueCursor;

public interface IssueCursorService{

	 /**
	   * Método que te devuelve todos los issues cursor.
	   * 
	   * @author FcoCrespo
	   */
	  List<IssueCursor> findAll();

	  /**
	   * Método para guardar un IssueCursor.
	   * 
	   * @author FcoCrespo
	   */
	  void saveIssueCursor(IssueCursor issueCursor);

	  /**
	   * Método para actualizar un CommitCursor.
	   * 
	   * @author FcoCrespo
	   */
	  void updateIssueCursor(IssueCursor issueCursor);

	  /**
	   * Método para borrar un IssueCursor.
	   * 
	   * @author FcoCrespo
	   */
	  void deleteIssueCursor(String issueCursorId);

	  /**
	   * Método para obtener un IssueCursor por su nextCursor.
	   * 
	   * @author FcoCrespo
	   */
	  IssueCursor findOne(String endCursor);

	  /**
	   * Método para obtener un IssueCursor por su repository.
	   * 
	   * @author FcoCrespo
	   */
	  IssueCursor getByRepositoryAndOwner(String repository, String owner);

}
