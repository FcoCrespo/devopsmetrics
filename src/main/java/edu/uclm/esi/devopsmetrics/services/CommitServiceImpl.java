package edu.uclm.esi.devopsmetrics.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
	private CommitRepository commitRepository;

	/**
	 * @author FcoCrespo
	 */
	@Autowired
	private final BranchService branchService;

	public CommitServiceImpl(final CommitRepository commitRepository, final BranchService branchService) {

		this.commitRepository = commitRepository;
		this.branchService = branchService;

	}

	/**
	 * @author FcoCrespo
	 */
	public Commit findByOid(final String oid) {

		final Optional<Commit> commit = commitRepository.findOne(oid);

		if (commit.isPresent()) {

			final Optional<Commit> commitOpt = commit;

			return commitOpt.get();

		} else {

			throw new CommitNotFoundException(oid);

		}

	}

	/**
	 * @author FcoCrespo
	 */
	public List<Commit> findAll() {

		final Optional<List<Commit>> commits = commitRepository.findAll();

		final List<Commit> commitsList = new ArrayList<Commit>();

		if (commits.isPresent()) {
			Commit commit;
			for (int i = 0; i < commits.get().size(); i++) {
				commit = commits.get().get(i);
				commitsList.add(commit);
			}

			return commitsList;
		} else {
			return Collections.emptyList();
		}

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
	public void deleteCommits(final String branchId) {

		commitRepository.deleteCommits(branchId);

	}

	@Override
	public Commit getCommitByOidyBranch(final String oid, final String branchId) {

		return commitRepository.findByOidyBranch(oid, branchId);

	}

	@Override
	public Commit getCommitByBranch(String branchId) {
		return commitRepository.findByBranch(branchId);
	}

	@Override
	public List<Commit> getAllCommitsByBranch(String branchId) {
		final Optional<List<Commit>> commits = commitRepository.findAllByBranch(branchId);

		final List<Commit> commitsList = new ArrayList<Commit>();

		if (commits.isPresent()) {
			Commit commit;
			for (int i = 0; i < commits.get().size(); i++) {
				commit = commits.get().get(i);
				commitsList.add(commit);
			}

			return commitsList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Commit> getAllCommitsByBranchAndAuthor(String branchId, String usergithub) {
		final Optional<List<Commit>> commits = commitRepository.findAllByBranchAndUserGithub(branchId, usergithub);

		final List<Commit> commitsList = new ArrayList<Commit>();

		if (commits.isPresent()) {
			Commit commit;
			for (int i = 0; i < commits.get().size(); i++) {
				commit = commits.get().get(i);
				commitsList.add(commit);
			}

			return commitsList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Commit> getAllByBranchBeginEndDate(String branchId, Instant beginDate, Instant endDate) {
		
		return commitRepository.findAllByBranchBeginEndDate(branchId, beginDate, endDate);
		
	}
	
	@Override
	public List<Commit> getAllByBranchBeginEndDateByAuthor(String branchId, Instant beginDate, Instant endDate, String githubuser) {
		
		return commitRepository.findAllByBranchBeginEndDateByAuthor(branchId, beginDate, endDate, githubuser);
		
	}

}
