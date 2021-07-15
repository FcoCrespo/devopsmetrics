package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.entities.Branch;

/**
 * Interfaz de BranchRepository.
 * 
 * @author FcoCrespo
 */
@Repository
public interface BranchRepository {
  /**
   * Método que te devuelve todos los branchs.
   * 
   * @author FcoCrespo
   */
  List<Branch> findAll();

  /**
   * Método para guardar un branch.
   * 
   * @author FcoCrespo
   */
  void saveBranch(Branch branch);

  /**
   * Método para actualizar un branch.
   * 
   * @author FcoCrespo
   */
  void updateBranch(Branch branch);

  /**
   * Método para borrar un branch.
   * 
   * @author FcoCrespo
   */
  void deleteBranch(String branchId);
  
  /**
   * Método para obtener un branch por su idGithub.
   * 
   * @author FcoCrespo
   */
  Optional<Branch> findOne(String idGithub);


  /**
   * Método para obtener un branch por repository, owner y name.
   * 
   * @author FcoCrespo
   */
  Branch findByRepositoryOwnerAndName(String repository, String owner, String name);
  
  /**
   * Método para obtener los branchs de un repositorio.
   * 
   * @author FcoCrespo
   */
  List<Branch> findAllbyRepositoryAndOwner(String repository, String owner);
  
  /**
   * Método para obtener la branch siguiente a la consultada
   * 
   * @author FcoCrespo
   */
  Branch findBeforeBranchByOrder(String repository, int order);
  
  
  /**
   * Método para obtener un branch por su idGithub.
   * 
   * @author FcoCrespo
   */
  Optional<Branch> findOneByName(String name);



}