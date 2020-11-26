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
	void deleteCommits(String branchId);

	/**
	 * @author FcoCrespo
	 */
	Commit getCommitByOidyBranch(String oid, String branchId);

	/**
	 * @author FcoCrespo
	 */
	Commit getCommitByBranch(String branchId);
	
	/**
	 * @author FcoCrespo
	 */
	List<Commit> getAllCommitsByBranch(String branchId);
	
	/**
	 * @author FcoCrespo
	 */
	List<Commit> getAllCommitsByBranchAndAuthor(String branchId, String usergithub);
	
	/**
	 * @author FcoCrespo
	 */
	List<Commit> getAllByBranchBeginEndDate(String branchId, Instant beginDate, Instant endDate);
	
	/**
	 * @author FcoCrespo
	 */
	List<Commit> getAllByBranchBeginEndDateByAuthor(String branchId, Instant beginDate, Instant endDate, String githubuser);
}