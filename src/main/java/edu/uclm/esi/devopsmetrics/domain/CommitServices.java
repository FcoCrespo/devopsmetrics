package edu.uclm.esi.devopsmetrics.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.services.CommitInfoService;
import edu.uclm.esi.devopsmetrics.services.CommitService;

@Service
@Scope("singleton")
public class CommitServices {
	

	private final CommitService commitService;
	private final CommitCursorService commitCursorService;
	private final CommitInfoService commitInfoService;
	
	public CommitServices(final CommitService commitService, CommitCursorService commitCursorService, CommitInfoService commitInfoService) {
		
		this.commitService = commitService;
		this.commitCursorService = commitCursorService;
		this.commitInfoService = commitInfoService;

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
}
