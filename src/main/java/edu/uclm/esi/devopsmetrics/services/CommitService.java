package edu.uclm.esi.devopsmetrics.services;

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
  void deleteCommit(String commitId);

  /**
   * @author FcoCrespo
   */
  Commit getCommitByOidyBranch(String oid, String branch);

 
}