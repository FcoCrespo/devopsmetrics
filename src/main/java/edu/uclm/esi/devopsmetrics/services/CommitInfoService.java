package edu.uclm.esi.devopsmetrics.services;

import java.util.List;

import edu.uclm.esi.devopsmetrics.entities.CommitInfo;

/**
 * @author FcoCrespo
 */
public interface CommitInfoService {
  /**
   * @author FcoCrespo
   */
  List<CommitInfo> findAll();

  /**
   * @author FcoCrespo
   */
  CommitInfo findByCommitId(String commitId);

  /**
   * @author FcoCrespo
   */
  void saveCommitInfo(CommitInfo commitInfo);

  /**
   * @author FcoCrespo
   */
  void updateCommitInfo(CommitInfo commitInfo);

  /**
   * @author FcoCrespo
   */
  void deleteCommitInfo(String commitId);

  
}