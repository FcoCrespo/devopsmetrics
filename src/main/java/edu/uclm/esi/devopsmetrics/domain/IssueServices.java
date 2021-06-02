package edu.uclm.esi.devopsmetrics.domain;

import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.services.IssueAssigneeService;
import edu.uclm.esi.devopsmetrics.services.IssueCursorService;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;
import edu.uclm.esi.devopsmetrics.services.IssueService;

@Service
public class IssueServices {

	private final IssueService issueService;
	private final IssueCursorService issueCursorService;
	private final IssueRepoService issueRepoService;
	private final IssueAssigneeService issueAssigneeService;

	/**
	 * @author FcoCrespo
	 */
	public IssueServices(final IssueService issueService, final IssueCursorService issueCursorService,
			final IssueRepoService issueRepoService, final IssueAssigneeService issueAssigneeService) {

		this.issueService = issueService;
		this.issueCursorService = issueCursorService;
		this.issueRepoService = issueRepoService;
		this.issueAssigneeService = issueAssigneeService;
	}

	public IssueService getIssueService() {
		return issueService;
	}

	public IssueCursorService getIssueCursorService() {
		return issueCursorService;
	}

	public IssueRepoService getIssueRepoService() {
		return issueRepoService;
	}

	public IssueAssigneeService getIssueAssigneeService() {
		return issueAssigneeService;
	}

}
