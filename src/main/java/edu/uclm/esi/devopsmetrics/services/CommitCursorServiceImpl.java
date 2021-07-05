package edu.uclm.esi.devopsmetrics.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.CommitCursorNotFoundException;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.repositories.CommitCursorRepository;


@Service("CommitCursorService")
/**
 * @author FcoCrespo
 */
@Transactional

public class CommitCursorServiceImpl implements CommitCursorService {

  /**
   * @author FcoCrespo
   */
  private CommitCursorRepository commitCursorRepository;

  /**
   * @author FcoCrespo
   */
  @Autowired

  public CommitCursorServiceImpl(final CommitCursorRepository commitCursorRepository) {

    this.commitCursorRepository = commitCursorRepository;

  }

  /**
   * @author FcoCrespo
   */
  public CommitCursor findByEndCursor(final String endCursor) {

    final Optional<CommitCursor> commit = commitCursorRepository.findOne(endCursor);

    if (commit.isPresent()) {

      final Optional<CommitCursor> userOpt = commit;

      return userOpt.get();

    } else {

      throw new CommitCursorNotFoundException(endCursor);

    }

  }

  /**
   * @author FcoCrespo
   */
  
public List<CommitCursor> findAll(){

    final Optional<List<CommitCursor>> commits = commitCursorRepository.findAll();

    final List<CommitCursor> commitsList = new ArrayList<CommitCursor>();
    
    if(commits.isPresent()) {
    	for (int i = 0; i < commits.get().size(); i++) {
            final CommitCursor commit = commits.get().get(i);
            commitsList.add(commit);
        }

        return commitsList;
    }
    else {
    	return Collections.emptyList();
    }
    

  }

  /**
   * @author FcoCrespo
   */
  public void saveCommitCursor(final CommitCursor commit) {

	  commitCursorRepository.saveCommitCursor(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void updateCommitCursor(final CommitCursor commit) {

	  commitCursorRepository.updateCommitCursor(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void deleteCommitCursor(final String branchIdGithub) {

	  commitCursorRepository.deleteCommitCursor(branchIdGithub);

  }

  @Override
  public CommitCursor getCommitCursorByBranchIdGithub(String branchIdGithub) {

    return commitCursorRepository.findByBranchIdGithub(branchIdGithub);

  }




}