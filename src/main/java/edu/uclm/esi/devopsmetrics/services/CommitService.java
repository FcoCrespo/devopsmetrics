package edu.uclm.esi.devopsmetrics.services;

import java.time.Instant;
import java.util.List;

import edu.uclm.esi.devopsmetrics.models.Commit;

/**
 * @author FcoCrespo
 */
public interface CommitService {
  /**
   * @author FcoCrespo
   */
  List<Commit> findAll();

  /**
   * @author FcoCrespo
   */
  Commit findByOid(String oid);

  /**
   * @author FcoCrespo
   */
  void saveCommit(Commit commit);

  /**
   * @author FcoCrespo
   */
  void updateCommit(Commit commit);

  /**
   * @author FcoCrespo
   */
  void deleteCommit(String repository);

  /**
   * @author FcoCrespo
   */
  Commit getCommitByOidyBranch(String oid, String branch);

  /**
   * @author FcoCrespo
   */
  List<Commit> getAllByBranch(String reponame, String branch);
  
  /**
   * @author FcoCrespo
   */
  List<Commit> getAllByBranchAndAuthorName(String reponame, String branch, String authorName);
  
  /**
   * @author FcoCrespo
   */
  List<Commit> getAllByBranchBeginEndDate(String reponame, String branch, Instant beginDate, Instant endDate);
  
  /**
   * @author FcoCrespo
   */
  List<Commit> getAllByBranchAuthorBeginEndDate(String reponame, String branch, String authorName, Instant beginDate, Instant endDate);
  
  /**
   * @author FcoCrespo
   */
  Commit getRepository(String repository);
 
}