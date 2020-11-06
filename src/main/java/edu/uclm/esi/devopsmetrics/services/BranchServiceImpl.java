package edu.uclm.esi.devopsmetrics.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.BranchNotFoundException;
import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.repositories.BranchRepository;
import edu.uclm.esi.devopsmetrics.repositories.BranchRepositoryImpl;


@Service("BranchService")
/**
 * @author FcoCrespo
 */
@Transactional

public class BranchServiceImpl implements BranchService {
  /**
   * @author FcoCrespo
   */
  private static final Log log = LogFactory.getLog(BranchServiceImpl.class);
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

      log.debug(String.format("Read branch '{}'", branch));

      final Optional<Branch> branchOpt = branch;

      return branchOpt.get();

    } else {

      throw new BranchNotFoundException(githubId);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<Branch> findAll() {

    final List<Branch> branches = branchRepository.findAll();
    
    return branches;

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
  public Branch getBranchByRepositoryyName(final String repository, final String name) {

    final Branch branch = branchRepository.findByRepositoryyName(repository, name);
    return branch;
    
  }

	@Override
	public List<Branch> getBranchesByRepository(String repository) {
		final List<Branch> branches = branchRepository.findAllbyRepository(repository);
		Collections.sort(branches);
		List <Branch> branchesRight = new ArrayList<Branch>();
		for(int i=0; i<branches.size(); i++) {
			if(branches.get(i).getOrder()!=-1) {
				branchesRight.add(branches.get(i));
			}
		}
		return branchesRight;
	}

	@Override
	public Branch getBeforeBranchByOrder(String repository, int order) {
		Branch branch = branchRepository.findBeforeBranchByOrder(repository, order);
		return branch;
	}




}