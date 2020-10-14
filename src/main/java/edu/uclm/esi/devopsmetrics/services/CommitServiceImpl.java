package edu.uclm.esi.devopsmetrics.services;


import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.devopsmetrics.exceptions.CommitNotFoundException;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.repositories.CommitRepository;


@Service("CommitService")
/**
 * @author FcoCrespo
 */
@Transactional

public class CommitServiceImpl implements CommitService {
  /**
   * @author FcoCrespo
   */
  private static final Log log = LogFactory.getLog(CommitServiceImpl.class);
  /**
   * @author FcoCrespo
   */
  private CommitRepository commitRepository;

  /**
   * @author FcoCrespo
   */
  @Autowired

  public CommitServiceImpl(final CommitRepository commitRepository) {

    this.commitRepository = commitRepository;

  }

  /**
   * @author FcoCrespo
   */
  public Commit findByOid(final String oid) {

    final Optional<Commit> commit = commitRepository.findOne(oid);

    if (commit.isPresent()) {

      log.debug(String.format("Read username '{}'", commit));

      final Optional<Commit> userOpt = commit;

      return userOpt.get();

    } else {

      throw new CommitNotFoundException(oid);

    }

  }

  /**
   * @author FcoCrespo
   */
  public List<Commit> findAll() {

    final Optional<List<Commit>> commits = commitRepository.findAll();

    final List<Commit> commitsList = null;
    
    for (int i = 0; i < commits.get().size(); i++) {
        final Commit commit = commits.get().get(i);
        commitsList.add(commit);
   }

    return commitsList;

  }

  /**
   * @author FcoCrespo
   */
  public void saveCommit(final Commit commit) {

	  commitRepository.saveCommit(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void updateCommit(final Commit commit) {

	  commitRepository.updateCommit(commit);

  }

  /**
   * @author FcoCrespo
   */
  public void deleteCommit(final String commitId) {

	  commitRepository.deleteCommit(commitId);

  }

  @Override
  public Commit getCommitByOidyBranch(final String oid, final String branch) {

    final Commit commit = commitRepository.findByOidyBranch(oid, branch);
    return commit;
    
  }




}