package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.entities.CommitCursor;

/**
 * @author FcoCrespo
 */
public interface CommitCursorService {
  /**
   * @author FcoCrespo
   */
  List<CommitCursor> findAll();

  /**
   * @author FcoCrespo
   */
  CommitCursor findByEndCursor(String endCursor);

  /**
   * @author FcoCrespo
   */
  void saveCommitCursor(CommitCursor commit);

  /**
   * @author FcoCrespo
   */
  void updateCommitCursor(CommitCursor commit);

  /**
   * @author FcoCrespo
   */
  void deleteCommitCursor(String branchIdGithub);

  /**
   * @author FcoCrespo
   */
  CommitCursor getCommitCursorByBranchIdGithub(String branchIdGithub);

 
}