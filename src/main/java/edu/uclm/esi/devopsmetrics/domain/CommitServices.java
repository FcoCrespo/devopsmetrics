package edu.uclm.esi.devopsmetrics.domain;

import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.services.CommitInfoService;
import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;

@Service
public class CommitServices {

	private final CommitService commitService;
	private final CommitCursorService commitCursorService;
	private final CommitInfoService commitInfoService;
	private final UserGithubService userGithubService;
	private final BranchService branchService;

	public CommitServices(final CommitService commitService, CommitCursorService commitCursorService,
			CommitInfoService commitInfoService, UserGithubService userGithubService,
			BranchService branchService) {

		this.commitService = commitService;
		this.commitCursorService = commitCursorService;
		this.commitInfoService = commitInfoService;
		this.userGithubService = userGithubService;
		this.branchService = branchService;

	}

	public CommitService getCommitService() {
		return commitService;
	}

	public CommitCursorService getCommitCursorService() {
		return commitCursorService;
	}

	public CommitInfoService getCommitInfoService() {
		return commitInfoService;
	}

	public UserGithubService getUserGithubService() {
		return userGithubService;
	}
	
	public BranchService getBranchService() {
		return branchService;
	}
}
