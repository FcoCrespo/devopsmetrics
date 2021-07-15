package edu.uclm.esi.devopsmetrics.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.entities.Branch;
import edu.uclm.esi.devopsmetrics.exceptions.BranchNotFoundException;
import edu.uclm.esi.devopsmetrics.repositories.BranchRepository;


@Service("BranchService")
/**
 * @author FcoCrespo
 */
@Transactional

public class BranchServiceImpl implements BranchService {
 
  /**
   * @author FcoCrespo
   */
  private BranchRepository branchRepository;

  /**
   * @author FcoCrespo
   */
  @Autowired

  public BranchServiceImpl(final BranchRepository branchRepository) {

    this.branchRepository = branchRepository;

  }

  /**
   * @author FcoCrespo
   */
  public Branch findById(final String githubId) {

    final Optional<Branch> branch = branchRepository.findOne(githubId);

    if (branch.isPresent()) {

      final Optional<Branch> branchOpt = branch;

      return branchOpt.get();

    } else {

      throw new BranchNotFoundException(githubId);

    }

  }
  
  
  /**
   * @author FcoCrespo
   */
  public Branch findByName(final String name) {

    final Optional<Branch> branch = branchRepository.findOneByName(name);

    if (branch.isPresent()) {

      final Optional<Branch> branchOpt = branch;

      return branchOpt.get();

    } else {

      return null;

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<Branch> findAll() {

    return branchRepository.findAll();

  }

  /**
   * @author FcoCrespo
   */
  public void saveBranch(final Branch branch) {

	  branchRepository.saveBranch(branch);

  }

  /**
   * @author FcoCrespo
   */
  public void updateBranch(final Branch branch) {

	  branchRepository.updateBranch(branch);

  }

  /**
   * @author FcoCrespo
   */
  public void deleteBranch(final String githubId) {

	  branchRepository.deleteBranch(githubId);

  }

  @Override
  public Branch getBranchByRepositoryyNameAndOwner(final String repository, String owner, final String name) {

    return branchRepository.findByRepositoryOwnerAndName(repository, owner, name);
    
  }

	@Override
	public List<Branch> getBranchesByRepositoryAndOwner(String repository, String owner, boolean order) {
		final List<Branch> branches = branchRepository.findAllbyRepositoryAndOwner(repository, owner);
		
		if(order) {
			Collections.sort(branches);
			List <Branch> branchesRight = new ArrayList<Branch>();
			for(int i=0; i<branches.size(); i++) {
				if(branches.get(i).getOrder()!=-1) {
					branchesRight.add(branches.get(i));
				}
			}
			return branchesRight;
		}
		else {
			return branches;
		}
		
	}

	@Override
	public Branch getBeforeBranchByOrder(String repository, int order) {
		return branchRepository.findBeforeBranchByOrder(repository, order);
	}

	@Override
	public List<Branch> getAllByRepositoryAndOwner(String repository, String owner) {
		return branchRepository.findAllbyRepositoryAndOwner(repository, owner);
	}




}