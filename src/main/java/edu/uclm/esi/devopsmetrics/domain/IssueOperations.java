package edu.uclm.esi.devopsmetrics.domain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.models.Issue;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;

@Service
@Scope("singleton")
public class IssueOperations {
	
	private static final Log LOG = LogFactory.getLog(IssueOperations.class);

	private final IssuesGithub issuesGithub;
	private final IssueServices issueServices;
	private final IssueRepoService issueRepoService;

	/**
	 * @author FcoCrespo
	 */
	public IssueOperations(final IssuesGithub issuesGithub, final IssueServices issueServices) {

		this.issuesGithub = issuesGithub;
		this.issueServices = issueServices;
		this.issueRepoService = this.issueServices.getIssueRepoService();
	}
	
	public void getIssues(String repository, String owner) throws IOException {


		IssueRepo issueDeRepo = null;

		IssueCursor issueCursor = null;

		boolean seguir = true;
		
		issueDeRepo = this.issueRepoService.getByRepoyOwner(repository, owner);
		
		if(issueDeRepo==null) {
			seguir=true;
		}
		else {
			seguir=false;
		}

		String filename;
		boolean initialStartCursorFind = false;

		String[] info = new String[3];
		info[0] = repository;
		info[1] = owner;

		if (seguir) {
			LOG.info("INTRODUCIENDO NUEVOS ISSUES DEL REPOSITORIO "+repository);
			filename = "src/main/resources/graphql/issues.graphql";
			
			this.issuesGithub.getNewRepositoryIssues(info, filename, issueCursor);
		
		} else {
			LOG.info("ACTUALIZANDO ISSUES DEL REPOSITORIO "+repository);
			filename = "src/main/resources/graphql/issues-cursor-before.graphql";
			info[2]=filename;
			
			List<Issue>issuesList = new ArrayList<Issue>();
			List<IssueRepo>issuesRepoList = new ArrayList<IssueRepo>();
			
			this.issuesGithub.updateRepositoryIssues(info, initialStartCursorFind, issuesList, issuesRepoList, issueCursor);
			
		}

	}

}
