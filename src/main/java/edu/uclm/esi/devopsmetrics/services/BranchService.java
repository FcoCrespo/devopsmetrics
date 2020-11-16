package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.models.Branch;

/**
 * @author FcoCrespo
 */
public interface BranchService {
  /**
   * @author FcoCrespo
   */
  List<Branch> findAll();

  /**
   * @author FcoCrespo
   */
  Branch findById(String githubId);
  
  /**
   * @author FcoCrespo
   */
  Branch findByName(String name);


  /**
   * @author FcoCrespo
   */
  void saveBranch(Branch branch);

  /**
   * @author FcoCrespo
   */
  void updateBranch(Branch branch);

  /**
   * @author FcoCrespo
   */
  void deleteBranch(String branchId);

  /**
   * @author FcoCrespo
   */
  Branch getBranchByRepositoryyName(String repository, String name);

  /**
   * @author FcoCrespo
   */
  List<Branch> getBranchesByRepository(String repository, boolean order);
  
  /**
   * @author FcoCrespo
   */
  Branch getBeforeBranchByOrder(String repository, int order);
 
}