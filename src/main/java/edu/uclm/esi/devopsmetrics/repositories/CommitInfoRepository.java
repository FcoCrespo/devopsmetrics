package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.CommitInfo;

/**
 * Interfaz de CommitInfoRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface CommitInfoRepository {
  /**
   * Método que te devuelve todos los commitInfo.
   * 
   * @author FcoCrespo
   */
  Optional<List<CommitInfo>> findAll();

  /**
   * Método para guardar un commitInfo de un commit.
   * 
   * @author FcoCrespo
   */
  void saveCommitInfo(CommitInfo commitInfo);

  /**
   * Método para actualizar un commitInfo.
   * 
   * @author FcoCrespo
   */
  void updateCommitInfo(CommitInfo commitInfo);

  /**
   * Método para borrar un commitInfo de un repositorio.
   * 
   * @author FcoCrespo
   */
  void deleteCommitInfo(String commitId);

  /**
   * Método para obtener un commitInfo por el id del commit al que se encuentra vinculado.
   * 
   * @author FcoCrespo
   */
  CommitInfo findByCommitId(String idCommit);
  

}