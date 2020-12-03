package edu.uclm.esi.devopsmetrics.domain;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.models.CommitInfo;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitService;
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
		int j = 0;
		
		issueDeRepo = this.issueRepoService.getByRepoyOwner(repository, owner);
		
		if(issueDeRepo==null) {
			seguir=true;
		}
		else {
			seguir=false;
		}

		String filename;
		boolean initialStartCursorFind = false;

		String[] info = new String[5];
		info[0] = repository;
		info[1] = owner;

		if (seguir) {
			LOG.info("INTRODUCIENDO NUEVOS ISSUES DEL REPOSITORIO "+repository);
			filename = "src/main/resources/graphql/issues.graphql";
			
			this.issuesGithub.getNewRepositoryIssues(info, filename, issueCursor);
		
		} else {
			LOG.info("ACTUALIZANDO ISSUES DEL REPOSITORIO "+repository);
			filename = "src/main/resources/graphql/issues-cursor-before.graphql";
			
			this.issuesGithub.updateRepositoryCommits(info, filename, initialStartCursorFind, issueCursor);
			
		}

	}

}
