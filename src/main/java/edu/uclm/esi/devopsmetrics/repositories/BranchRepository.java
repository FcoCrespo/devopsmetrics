package edu.uclm.esi.devopsmetrics.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uclm.esi.devopsmetrics.models.Branch;

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
   * Método para obtener un branch por repository y name.
   * 
   * @author FcoCrespo
   */
  Branch findByRepositoryyName(String repository, String name);
  
  /**
   * Método para obtener los branchs de un repositorio.
   * 
   * @author FcoCrespo
   */
  List<Branch> findAllbyRepository(String repository);
  
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