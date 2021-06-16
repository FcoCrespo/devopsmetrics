package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.Response;

import edu.uclm.esi.devopsmetrics.services.IssueAssigneeService;
import edu.uclm.esi.devopsmetrics.services.IssueCursorService;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;
import edu.uclm.esi.devopsmetrics.services.IssueService;
import edu.uclm.esi.devopsmetrics.services.UserGithubReposService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;
import edu.uclm.esi.devopsmetrics.models.Issue;
import edu.uclm.esi.devopsmetrics.models.IssueAssignee;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.models.UserGithubRepos;

@Service
public class IssuesGithub {

	private static final Log LOG = LogFactory.getLog(IssuesGithub.class);

	private final IssueServices issueServices;
	private final IssueService issueService;
	private final IssueCursorService issueCursorService;
	private final IssueRepoService issueRepoService;
	private final IssueAssigneeService issueAssigneeService;
	private final UserGithubOperations userGithubOperations;
	private final UserGithubReposService userGithubReposService;
	private final ResponseHTTP response;

	private String cursorString;
	private String repositoryString;
	private String emailString;
	private String avatarUrlString;
	private String loginString;
	private String issuesString;
	private String nodesString;
	private String closedString;
	private String assigneesString;

	private String graphqlUri;
	private String filenameCursor;
	
	private String ownerStr= "owner";

	/**
	 * @author FcoCrespo
	 */
	public IssuesGithub(final IssueServices issueServices, final UserGithubOperations userGithubOperations,
			final ResponseHTTP response, final UserGithubReposService userGithubReposService) {

		this.issueServices = issueServices;
		this.issueService = this.issueServices.getIssueService();
		this.issueCursorService = this.issueServices.getIssueCursorService();
		this.issueRepoService = this.issueServices.getIssueRepoService();
		this.issueAssigneeService = this.issueServices.getIssueAssigneeService();
		this.userGithubReposService = userGithubReposService;
		this.userGithubOperations = userGithubOperations;
		this.response = response;
		this.graphqlUri = "https://api.github.com/graphql";
		this.filenameCursor = "src/main/resources/graphql/issues-cursor.graphql";
		this.cursorString = "cursor";
		this.repositoryString = "repository";
		this.emailString = "email";
		this.avatarUrlString = "avatarUrl";
		this.loginString = "login";
		this.issuesString = "issues";
		this.nodesString = "nodes";
		this.closedString = "closedAt";
		this.assigneesString="assignees";

	}

	public void getNewRepositoryIssues(String[] info, String filename, IssueCursor issueCursor) throws IOException {

		String jsonData;
		JsonNode jsonNode;
		JsonNode nodes;
		JsonNode parameterNode;
		Response responseGiven;
		Iterator<JsonNode> iter;

		String graphqlPayload;
		ObjectNode variables;

		String[] variablesPut = new String[3];
		variablesPut[0] = info[0];
		variablesPut[1] = info[1];
		variablesPut[2] = filename;

		File file = new File(filename);

		LOG.info(file.getPath());
		variables = getVariables(variablesPut, issueCursor, null);

		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();
		jsonNode = new ObjectMapper().readTree(jsonData);

		issueCursor = updateIssueCursor(jsonNode, info[0]);

		nodes = jsonNode.path("data").path(this.repositoryString).path(this.issuesString).path(this.nodesString);
		iter = nodes.iterator();
		parameterNode = iter.next();

		Object[] result;

		Issue issue;
		IssueRepo issueRepo;

		while (iter.hasNext()) {
			result = introducirIssue(parameterNode, info);
			issue = (Issue) result[0];
			issueRepo = (IssueRepo) result[1];

			this.issueService.saveIssue(issue);

			this.issueRepoService.saveIssueRepo(issueRepo);

			parameterNode = iter.next();

			if (!iter.hasNext()) {
				result = introducirIssue(parameterNode, info);
				issue = (Issue) result[0];
				issueRepo = (IssueRepo) result[1];

				this.issueService.saveIssue(issue);

				this.issueRepoService.saveIssueRepo(issueRepo);

				if (issueCursor != null) {
					issueCursor.setIdLastIssue(issue.getId());
					this.issueCursorService.updateIssueCursor(issueCursor);
				}
			}
		}

		if (issueCursor != null && issueCursor.isHasNextPage()) {
			filename = this.filenameCursor;
			getNewRepositoryIssues(info, filename, issueCursor);

		}

	}

	public String updateRepositoryIssues(String[] info, boolean initialStartCursorFind, List<Issue> issuesList,
			List<IssueRepo> issuesRepoList, IssueCursor issueCursor) throws IOException {
		String graphqlPayload;

		ObjectNode variables;

		String jsonData;
		JsonNode jsonNode;
		JsonNode nodes;
		JsonNode parameterNode;
		Response responseGiven;
		Iterator<JsonNode> iter;

		File file = new File(info[2]);

		IssueCursor issueCursorInitial = this.issueCursorService.getByRepository(info[0]);

		String[] variablesPut = new String[3];
		variablesPut[0] = info[0];
		variablesPut[1] = info[1];
		variablesPut[2] = info[2];

		variables = getVariablesUpdate(variablesPut, issueCursor, issueCursorInitial.getEndCursor());

		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();

		jsonNode = new ObjectMapper().readTree(jsonData);

		issueCursor = updateIssueCursor(jsonNode, info[0]);

		if (issueCursor == null) {
			return "Fin.";
		}

		nodes = jsonNode.path("data").path(this.repositoryString).path(this.issuesString).path(this.nodesString);

		iter = nodes.iterator();

		parameterNode = iter.next();

		Object[] result;

		Issue issue;
		IssueRepo issueRepo;

		if (iter.hasNext()) {
			while (iter.hasNext()) {

				result = introducirIssue(parameterNode, info);
				issue = (Issue) result[0];
				issueRepo = (IssueRepo) result[1];


				parameterNode = iter.next();

				issuesList.add(issue);
				issuesRepoList.add(issueRepo);
				

			}
			if (!iter.hasNext()) {
				result = introducirIssue(parameterNode, info);
				issue = (Issue) result[0];
				issueRepo = (IssueRepo) result[1];

				actualizarCursor(issueCursor, issue);
				issuesList.add(issue);
				issuesRepoList.add(issueRepo);
				
				saveUpdateIssues(issuesList, issuesRepoList);
				
			}

		} else {
			result = introducirIssue(parameterNode, info);
			issue = (Issue) result[0];
			issueRepo = (IssueRepo) result[1];

		    actualizarCursor(issueCursor, issue);
		    
		    this.issueService.saveIssue(issue);
		    this.issueRepoService.saveIssueRepo(issueRepo);
			
		}

		actualizarIssuesRepository(info, initialStartCursorFind, issuesList, issuesRepoList, issueCursor);
		return "Ok.";

	}
	
	private void saveUpdateIssues(List<Issue> issuesList, List<IssueRepo> issuesRepoList) {
		for(int i=0; i<issuesList.size(); i++) {
			this.issueService.saveIssue(issuesList.get(i));
		    this.issueRepoService.saveIssueRepo(issuesRepoList.get(i));
		}		
	}

	private IssueCursor updateIssueCursor(JsonNode jsonNode, String reponame) {
		JsonNode cursorNode = jsonNode.path("data").path(this.repositoryString).path(this.issuesString)
				.path("pageInfo");

		LOG.info(cursorNode.toString());

		boolean hasNextPage = cursorNode.get("hasNextPage").booleanValue();
		String endCursor = cursorNode.get("endCursor").textValue();
		String startCursor = cursorNode.get("startCursor").textValue();

		if (endCursor == null || startCursor == null) {
			return null;
		}

		IssueCursor issueCursor = this.issueCursorService.getByRepository(reponame);

		if (issueCursor == null) {
			issueCursor = new IssueCursor(hasNextPage, endCursor, startCursor, reponame, null);
			this.issueCursorService.saveIssueCursor(issueCursor);
		} else {
			issueCursor.setHasNextPage(hasNextPage);
			issueCursor.setStartCursor(startCursor);
			issueCursor.setEndCursor(endCursor);

			this.issueCursorService.updateIssueCursor(issueCursor);
		}

		LOG.info("IssueCursor-> end : " + issueCursor.getEndCursor() + " start: " + issueCursor.getStartCursor());

		return issueCursor;
	}
	
	private ObjectNode getVariablesUpdate(String[] variablesPut, IssueCursor issueCursor, String endCursor) {
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("repo", variablesPut[0]);
		variables.put(this.ownerStr, variablesPut[1]);
		if (endCursor != null) {
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				variables.put(this.cursorString, issueCursor.getEndCursor());
			} else {
				variables.put(this.cursorString, endCursor);
			}
		} else {
			LOG.info(variablesPut[2]);
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				LOG.info("Cursor end actual: " + issueCursor.getEndCursor());
				variables.put(this.cursorString, issueCursor.getEndCursor());
			}
		}
		LOG.info(variables.toPrettyString());
		return variables;
	}

	public void actualizarValores(String[] info, String filename, IssueCursor issueCursor) throws IOException {

		String jsonData;
		JsonNode jsonNode;
		JsonNode nodes;
		JsonNode parameterNode;
		Response responseGiven;
		Iterator<JsonNode> iter;

		String graphqlPayload;
		ObjectNode variables;

		String[] variablesPut = new String[3];
		variablesPut[0] = info[0];
		variablesPut[1] = info[1];
		variablesPut[2] = filename;

		File file = new File(filename);

		LOG.info(file.getPath());
		variables = getVariables(variablesPut, issueCursor, null);

		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();
		jsonNode = new ObjectMapper().readTree(jsonData);
		
		issueCursor=obtenerIssueCursor(jsonNode, info[0]);

		nodes = jsonNode.path("data").path(this.repositoryString).path(this.issuesString).path(this.nodesString);
		iter = nodes.iterator();
		parameterNode = iter.next();
		
		

		Issue issue;

		if (iter.hasNext()) {
			while (iter.hasNext()) {

				issue = this.issueService.findOne(parameterNode.get("id").asText());

				actualizarIssue(parameterNode, issue);

				parameterNode = iter.next();

				if (!iter.hasNext()) {
					issue = this.issueService.findOne(parameterNode.get("id").asText());

					actualizarIssue(parameterNode, issue);

				}
			}

		} else {
			issue = this.issueService.findOne(parameterNode.get("id").asText());

			actualizarIssue(parameterNode, issue);
		}

		if (issueCursor != null && issueCursor.isHasNextPage()) {
			filename = this.filenameCursor;
			actualizarValores(info, filename, issueCursor);
		}

	}
	
	private IssueCursor obtenerIssueCursor(JsonNode jsonNode, String reponame) {
		JsonNode cursorNode = jsonNode.path("data").path(this.repositoryString).path(this.issuesString)
				.path("pageInfo");

		LOG.info(cursorNode.toString());
		
		IssueCursor issueCursor;

		boolean hasNextPage = cursorNode.get("hasNextPage").booleanValue();
		String endCursor = cursorNode.get("endCursor").textValue();
		String startCursor = cursorNode.get("startCursor").textValue();

		if (endCursor == null || startCursor == null) {
			return null;
		}
		
		else {
			issueCursor = new IssueCursor(hasNextPage, endCursor, startCursor, reponame, null);
		}


		return issueCursor;
	}
	
	private void actualizarIssue(JsonNode parameterNode, Issue issue) {

		
		String title = comprobarValorString(parameterNode, "title");
		String body = comprobarValorString(parameterNode, "body");
		String state = comprobarValorString(parameterNode, "state");

		String closedAtExtraido = comprobarValorString(parameterNode, this.closedString);

		Instant closedAt = null;

		if (closedAtExtraido != null) {
			closedAt = Instant.parse(closedAtExtraido).plus(1, ChronoUnit.HOURS);
		}
		
		issue.setState(state);
		issue.setTitle(title);
		issue.setBody(body);
		
		issue.setClosedAt(closedAt);
		
		UserGithub userGithubAsignee = null;
		Iterator<JsonNode> iter;
		
		JsonNode nodesAssignees;
		nodesAssignees = parameterNode.path(this.assigneesString);

		String[] asigneeValues;

		boolean assigneesNull = false;

		JsonNode parameterNodeAssignee;
		

		if (comprobarValorInt(nodesAssignees, "totalCount") == 0) {
			assigneesNull = true;
		}
		if (!assigneesNull) {

			nodesAssignees = parameterNode.path(this.assigneesString).path(this.nodesString);

			iter = nodesAssignees.iterator();

			parameterNodeAssignee = iter.next();

			if (iter.hasNext()) {
				while (iter.hasNext()) {

					asigneeValues = obtenerDatos(parameterNodeAssignee);

					userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);
					
					comprobarExisteIssueAssignee(userGithubAsignee, issue);
					
					parameterNodeAssignee = iter.next();

				}
				if (!iter.hasNext()) {

					asigneeValues = obtenerDatos(parameterNodeAssignee);

					userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);

					comprobarExisteIssueAssignee(userGithubAsignee, issue);

				}
			} else {

				asigneeValues = obtenerDatos(parameterNodeAssignee);

				userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);

				
				comprobarExisteIssueAssignee(userGithubAsignee, issue);
				

			}

		}
		else {
			List<IssueAssignee>listaAsignees=this.issueAssigneeService.getAllByIdIssue(issue.getId());
			
			for(int i=0; i<listaAsignees.size(); i++) {
				this.issueAssigneeService.deleteIssueAssignee(listaAsignees.get(i).getId());
			}
		}

		this.issueService.updateIssue(issue);
	}
	
	private ObjectNode getVariables(String[] variablesPut, IssueCursor issueCursor, String startCursor) {
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("repo", variablesPut[0]);
		variables.put(this.ownerStr, variablesPut[1]);
		if (startCursor != null) {
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				variables.put(this.cursorString, issueCursor.getEndCursor());
			} else {
				variables.put(this.cursorString, startCursor);
			}
		} else {
			LOG.info(variablesPut[2]);
			if (variablesPut[2].equals(this.filenameCursor) && issueCursor != null) {
				LOG.info("Cursor end actual: " + issueCursor.getEndCursor());
				variables.put(this.cursorString, issueCursor.getEndCursor());
			}
		}
		LOG.info(variables.toPrettyString());
		return variables;
	}

	

	

	private void comprobarExisteIssueAssignee(UserGithub userGithubAsignee, Issue issue) {
		IssueAssignee issueAssignee = this.issueAssigneeService.getByAssigneeAndIssue(userGithubAsignee.getId(), issue.getId());
		if(issueAssignee==null) {
			
			issueAssignee = new IssueAssignee(issue.getId(), userGithubAsignee.getId());

			this.issueAssigneeService.saveIssueAssignee(issueAssignee);
			
		}
		
	}

	private void actualizarCursor(IssueCursor issueCursor, Issue issue) {
		if (issue != null) {
			issueCursor.setIdLastIssue(issue.getId());
			this.issueCursorService.updateIssueCursor(issueCursor);
		}
	}

	private void actualizarIssuesRepository(String[] info, boolean initialStartCursorFind, List<Issue> issuesList,
			List<IssueRepo> issuesRepoList, IssueCursor issueCursor) throws IOException {

		String newFilename = "";

		if (initialStartCursorFind) {
			saveIssuesRepository(issuesList, issuesRepoList);
		} else {
			newFilename = "src/main/resources/graphql/issues-cursor.graphql";
			info[2] = newFilename;
			updateRepositoryIssues(info, initialStartCursorFind, issuesList, issuesRepoList, issueCursor);
		}

	}

	private void saveIssuesRepository(List<Issue> issuesList, List<IssueRepo> issuesRepoList) {
		for (int j = 0; j < issuesList.size(); j++) {
			this.issueService.saveIssue(issuesList.get(j));
		}

		for (int i = 0; i < issuesRepoList.size(); i++) {
			this.issueRepoService.saveIssueRepo(issuesRepoList.get(i));
		}
	}
	
	

	private Object[] introducirIssue(JsonNode parameterNode, String [] info) {
		Issue issue = null;
		IssueRepo issueRepo = null;
		UserGithub userGithubAuthor = null;
		UserGithub userGithubAsignee = null;
		Iterator<JsonNode> iter;

		JsonNode nodeRepository;
		nodeRepository = parameterNode.path(this.repositoryString);

		JsonNode nodeOwner;
		nodeOwner = parameterNode.path(this.repositoryString).path(this.ownerStr);

		String owner = comprobarValorString(nodeOwner, this.loginString);

		String repository = comprobarValorString(nodeRepository, "name");

		String title = comprobarValorString(parameterNode, "title");
		String body = comprobarValorString(parameterNode, "body");
		String state = comprobarValorString(parameterNode, "state");
		String idGithub = comprobarValorString(parameterNode, "id");

		String createdAtExtraido = comprobarValorString(parameterNode, "createdAt");
		String closedAtExtraido = comprobarValorString(parameterNode, this.closedString);

		Instant createdAt = null;
		Instant closedAt = null;

		if (createdAtExtraido != null) {
			createdAt = Instant.parse(createdAtExtraido).plus(1, ChronoUnit.HOURS);
		}

		if (closedAtExtraido != null) {
			closedAt = Instant.parse(closedAtExtraido).plus(1, ChronoUnit.HOURS);
		}

		JsonNode nodeAuthor;
		nodeAuthor = parameterNode.path("author");

		String authorLogin = comprobarValorString(nodeAuthor, this.loginString);

		String authorName = comprobarValorString(nodeAuthor, "name");
		String authorId = comprobarValorString(nodeAuthor, "id");
		String authorEmail = comprobarValorString(nodeAuthor, this.emailString);
		String authorAvatarURL = comprobarValorString(nodeAuthor, this.avatarUrlString);

		String[] authorValues = new String[5];
		authorValues[0] = authorLogin;
		authorValues[1] = authorName;
		authorValues[2] = authorId;
		authorValues[3] = authorEmail;
		authorValues[4] = authorAvatarURL;

		userGithubAuthor = this.userGithubOperations.saveAuthor(authorValues);
		
		guardarUsuarioGithubRepo(userGithubAuthor, info);

		JsonNode nodesAssignees;
		nodesAssignees = parameterNode.path(this.assigneesString);

		String[] asigneeValues;

		boolean assigneesNull = false;

		JsonNode parameterNodeAssignee;

		issue = new Issue(state, title, body, createdAt, closedAt);
		issue.setId(idGithub);

		issueRepo = new IssueRepo(repository, owner, userGithubAuthor.getId());
		issueRepo.setId(idGithub);
		
		if (comprobarValorInt(nodesAssignees, "totalCount") == 0) {
			assigneesNull = true;
		}
		if (!assigneesNull) {

			nodesAssignees = parameterNode.path(this.assigneesString).path(this.nodesString);

			iter = nodesAssignees.iterator();

			parameterNodeAssignee = iter.next();

			if (iter.hasNext()) {
				while (iter.hasNext()) {

					asigneeValues = obtenerDatos(parameterNodeAssignee);

					userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);

					comprobarExisteIssueAssignee(userGithubAsignee, issue);

					parameterNodeAssignee = iter.next();

				}
				if (!iter.hasNext()) {

					asigneeValues = obtenerDatos(parameterNodeAssignee);

					userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);

					comprobarExisteIssueAssignee(userGithubAsignee, issue);

				}
			} else {

				asigneeValues = obtenerDatos(parameterNodeAssignee);

				userGithubAsignee = this.userGithubOperations.saveAuthor(asigneeValues);

				comprobarExisteIssueAssignee(userGithubAsignee, issue);

			}

		}

		Object[] result = new Object[2];

		result[0] = issue;
		result[1] = issueRepo;

		return result;
	}
	
	private void guardarUsuarioGithubRepo(UserGithub userGithub, String[] info) {
		UserGithubRepos usergithubrepos = this.userGithubReposService.findByUserGithubReposData(userGithub.getId(), info[0], info[1]);
		
		if(usergithubrepos==null) {
			usergithubrepos = new UserGithubRepos(userGithub.getId(), info[0], info[1]);
			this.userGithubReposService.saveUserGithubRepos(usergithubrepos);
		}
	}


	private String[] obtenerDatos(JsonNode parameterNodeAssignee) {

		String asigneeLogin;
		String asigneeName;
		String asigneeId;
		String asigneeAvatarURL;
		String asigneeEmail;

		String[] asigneeValues = new String[5];

		asigneeName = comprobarValorString(parameterNodeAssignee, "name");
		asigneeEmail = comprobarValorString(parameterNodeAssignee, this.emailString);
		asigneeAvatarURL = comprobarValorString(parameterNodeAssignee, this.avatarUrlString);
		asigneeId = comprobarValorString(parameterNodeAssignee, "id");
		asigneeLogin = comprobarValorString(parameterNodeAssignee, this.loginString);

		asigneeValues[0] = asigneeLogin;
		asigneeValues[1] = asigneeName;
		asigneeValues[2] = asigneeId;
		asigneeValues[3] = asigneeEmail;
		asigneeValues[4] = asigneeAvatarURL;

		return asigneeValues;

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

	

	

}