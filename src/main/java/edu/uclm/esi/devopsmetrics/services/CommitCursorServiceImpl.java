package edu.uclm.esi.devopsmetrics.services;


import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
  private static final Log log = LogFactory.getLog(CommitServiceImpl.class);
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

      log.debug(String.format("Read username '{}'", commit));

      final Optional<CommitCursor> userOpt = commit;

      return userOpt.get();

    } else {

      throw new CommitCursorNotFoundException(endCursor);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<CommitCursor> findAll() {

    final Optional<List<CommitCursor>> commits = commitCursorRepository.findAll();

    final List<CommitCursor> commitsList = null;
    
    for (int i = 0; i < commits.get().size(); i++) {
        final CommitCursor commit = commits.get().get(i);
        commitsList.add(commit);
   }

    return commitsList;

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
  public void deleteCommitCursor(final String commitId) {

	  commitCursorRepository.deleteCommitCursor(commitId);

  }

  @Override
  public CommitCursor getCommitCursorByEndCursoryHasNextPage(final String endCursor, final String hasNextPage, String branch, String repository) {

    final CommitCursor commit = commitCursorRepository.findByEndCursoryHasNextPage(endCursor, hasNextPage, branch, repository);
    return commit;
    
  }




}