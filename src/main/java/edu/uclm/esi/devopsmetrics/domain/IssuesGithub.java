package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.Response;

import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.services.CommitInfoService;
import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.services.IssueAssigneeService;
import edu.uclm.esi.devopsmetrics.services.IssueCursorService;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;
import edu.uclm.esi.devopsmetrics.services.IssueService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;

import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.models.CommitInfo;
import edu.uclm.esi.devopsmetrics.models.Issue;
import edu.uclm.esi.devopsmetrics.models.IssueAssignee;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.models.UserGithub;

@Service
@Scope("singleton")
public class IssuesGithub {
	
	private static final Log LOG = LogFactory.getLog(IssuesGithub.class);

	private final IssueServices issueServices;
	private final IssueService issueService;
	private final IssueCursorService issueCursorService;
	private final IssueRepoService issueRepoService;
	private final IssueAssigneeService issueAssigneeService;
	private final UserGithubOperations userGithubOperations;
	private final ResponseHTTP response;

	private String cursorString;
	private String repositoryString;

	private String graphqlUri;
	private String filenameCursor;

	/**
	 * @author FcoCrespo
	 */
	public IssuesGithub(final IssueServices issueServices, final UserGithubOperations userGithubOperations, final ResponseHTTP response) {

		this.issueServices = issueServices;
		this.issueService = this.issueServices.getIssueService();
		this.issueCursorService = this.issueServices.getIssueCursorService();
		this.issueRepoService = this.issueServices.getIssueRepoService();
		this.issueAssigneeService = this.issueServices.getIssueAssigneeService();
		this.userGithubOperations = userGithubOperations;
		this.response = response;
		this.graphqlUri = "https://api.github.com/graphql";
		this.filenameCursor = "src/main/resources/graphql/issues-cursor.graphql";
		this.cursorString = "cursor";
		this.repositoryString = "repository";

	}

	public void getNewRepositoryIssues(String[] info, String filename, IssueCursor issueCursor) throws IOException {
		
		String graphqlPayload;
		ObjectNode variables;

		String jsonData;
		JsonNode jsonNode;
		JsonNode nodes;
		JsonNode parameterNode;
		Response responseGiven;
		Iterator<JsonNode> iter;

		File file = new File(filename);

		String[] variablesPut = new String[3];
		variablesPut[0] = info[0];
		variablesPut[1] = info[1];
		variablesPut[2] = filename;
		
		LOG.info(file.getPath());
		variables = getVariables(variablesPut, info[2], issueCursor, null);
		
		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();
		jsonNode = new ObjectMapper().readTree(jsonData);
		
		LOG.info(jsonData);

		issueCursor = updateIssueCursor(jsonNode, info[0]);
		
		nodes = jsonNode.path("data").path(this.repositoryString).path("issues").path("nodes");
		iter = nodes.iterator();
		parameterNode = iter.next();

		Object [] result;
		
		Issue issue;
		IssueRepo issueRepo;
		
		while (iter.hasNext()) {
			result = introducirIssue(parameterNode);
			issue = (Issue) result[0];
			issueRepo = (IssueRepo) result[1];
			
			//this.issueService.saveIssue(issue);
			
			//this.issueRepoService.saveIssueRepo(issueRepo);
		
			parameterNode = iter.next();

			if (!iter.hasNext()) {
				result = introducirIssue(parameterNode);
				issue = (Issue) result[0];
				issueRepo = (IssueRepo) result[1];
				
				//this.issueService.saveIssue(issue);
				
				//this.issueRepoService.saveIssueRepo(issueRepo);
			}
		}

		if (issueCursor.isHasNextPage()) {
			filename = this.filenameCursor;
			getNewRepositoryIssues(info, filename, issueCursor);

		}

	}
	
	public void updateRepositoryCommits(String[] info, String filename, boolean initialStartCursorFind,
			IssueCursor issueCursor) {
		// TODO Auto-generated method stub
		
	}

	private Object[] introducirIssue(JsonNode parameterNode) {
		Issue issue = null;
		IssueRepo issueRepo = null;
		IssueAssignee issueAssignee = null;
		UserGithub userGithub = null;
		Iterator<JsonNode> iter;
		
		String title = comprobarValorString(parameterNode, "title");
		System.out.println(title);
		
		JsonNode nodesAssignees;
		JsonNode parameterAssignee;
		nodesAssignees = parameterNode.path("assignees");
		
		if(comprobarValorInt(nodesAssignees, "totalCount")==0) {
			System.out.println("VACIO SIN ASIGNEES");
		}
		else {
			nodesAssignees = parameterNode.path("assignees").path("nodes");
			
			iter = nodesAssignees.iterator();
			
			int i = 0;
			String login;
			
			parameterAssignee = iter.next();
			
			if(iter.hasNext()) {
				while (iter.hasNext()) {
					
					login = comprobarValorString(parameterAssignee, "login");
					
					System.out.println("login: "+login+" , nº "+i);
				
					parameterAssignee = iter.next();

					i++;
				}
				if (!iter.hasNext()) {
					login = comprobarValorString(parameterAssignee, "login");
					
					System.out.println("login: "+login+" , issue nº "+i);
				}
			}
			else{
				login = comprobarValorString(parameterAssignee, "login");
				
				System.out.println("login: "+login+" , nº "+i);
			}
			
			
			
		}
		
		
		Object [] result = new Object[2];
		
		result[0] = issue;
		result[1] = issueRepo;
		
		return result;
	}

	private String comprobarValorString(JsonNode parameterNode, String textValue) {

		if (parameterNode.get(textValue) == null) {
			return "";
		} else {
			return parameterNode.get(textValue).textValue();
		}

	}
	
	private int comprobarValorInt(JsonNode parameterNode, String intValue) {

		if (parameterNode.get(intValue) == null) {
			return 0;
		} else {
			return parameterNode.get(intValue).intValue();
		}

	}

	private IssueCursor updateIssueCursor(JsonNode jsonNode, String reponame) {
		JsonNode cursorNode = jsonNode.path("data").path("repository").path("issues").path("pageInfo");
		
		LOG.info(cursorNode.toString());

		boolean hasNextPage = cursorNode.get("hasNextPage").booleanValue();
		String endCursor = cursorNode.get("endCursor").textValue();
		String startCursor = cursorNode.get("startCursor").textValue();

		IssueCursor issueCursor = this.issueCursorService.getByRepository(reponame);

		if (issueCursor == null) {
			issueCursor = new IssueCursor(hasNextPage, endCursor, startCursor, reponame);
			this.issueCursorService.saveIssueCursor(issueCursor);
		} else {
			issueCursor.setHasNextPage(hasNextPage);
			issueCursor.setStartCursor(startCursor);
			issueCursor.setEndCursor(endCursor);

			this.issueCursorService.updateIssueCursor(issueCursor);
		}

		LOG.info("IssueCursor-> end : "+ issueCursor.getEndCursor()+ " start: "+issueCursor.getStartCursor());

		return issueCursor;
	}

	private ObjectNode getVariables(String[] variablesPut, String string, IssueCursor issueCursor, String startCursor) {
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("repo", variablesPut[0]);
		variables.put("owner", variablesPut[1]);	
		if (startCursor != null) {
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				variables.put(this.cursorString, issueCursor.getEndCursor());
			} else {
				variables.put(this.cursorString, startCursor);
			}
		} else {
			LOG.info(variablesPut[2]);
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				LOG.info("Cursor end actual: "+issueCursor.getEndCursor());
				variables.put(this.cursorString, issueCursor.getEndCursor());
			}
		}
		LOG.info(variables.toPrettyString());
		return variables;
	}

	

	
	
	
}