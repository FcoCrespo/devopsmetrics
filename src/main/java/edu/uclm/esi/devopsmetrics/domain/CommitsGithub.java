package edu.uclm.esi.devopsmetrics.domain;

import java.io.File;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

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
import edu.uclm.esi.devopsmetrics.services.UserGithubService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;

import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.models.CommitInfo;
import edu.uclm.esi.devopsmetrics.models.UserGithub;

@Service
@Scope("singleton")
public class CommitsGithub {
	
	private static final Log LOG = LogFactory.getLog(CommitsGithub.class);

	private final CommitService commitService;
	private final CommitCursorService commitCursorService;
	private final CommitInfoService commitInfoService;
	private final UserGithubService userGithubService;
	private final ResponseHTTP response;

	private String cursorString;
	private String repositoryString;
	private String targetString;
	private String historyString;

	private String graphqlUri;
	private String filenameCursor;

	/**
	 * @author FcoCrespo
	 */
	public CommitsGithub(final CommitService commitService, final CommitCursorService commitCursorService,
			final CommitInfoService commitInfoService, final UserGithubService userGithubService,
			final ResponseHTTP response) {

		this.commitService = commitService;
		this.commitCursorService = commitCursorService;
		this.commitInfoService = commitInfoService;
		this.userGithubService = userGithubService;
		this.response = response;
		this.graphqlUri = "https://api.github.com/graphql";
		this.filenameCursor = "src/main/resources/graphql/commits-cursor.graphql";
		this.cursorString = "cursor";
		this.repositoryString = "repository";
		this.targetString="target";
		this.historyString="history";

	}

	public void getNewRepositoryCommits(String[] info, String filename, CommitCursor commitCursor) throws IOException {

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
		variables = getVariables(variablesPut, info[2], commitCursor, null);

		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();
		jsonNode = new ObjectMapper().readTree(jsonData);
		
		LOG.info(jsonData);

		commitCursor = updateCommitCursor(jsonNode, info[2], info[0]);

		nodes = jsonNode.path("data").path(this.repositoryString).path("ref").path(this.targetString).path(this.historyString).path("nodes");
		iter = nodes.iterator();
		parameterNode = iter.next();
		
		Commit commit;
		CommitInfo commitInfo;

		Object [] result;
		
		while (iter.hasNext()) {

			result = introducirCommit(parameterNode, info[3]);
			commit = (Commit) result[0];
			commitInfo = (CommitInfo) result[1];
			commitService.saveCommit(commit);
			
			comprobarSaveCommitInfo(commitInfo);
		
			parameterNode = iter.next();

			if (!iter.hasNext()) {
				result = introducirCommit(parameterNode, info[3]);
				commit = (Commit) result[0];
				commitInfo = (CommitInfo) result[1];
				commitService.saveCommit(commit);
				
				comprobarSaveCommitInfo(commitInfo);
			}
		}

		if (commitCursor.getHasNextPage()) {
			filename = this.filenameCursor;
			getNewRepositoryCommits(info, filename, commitCursor);

		}
	}

	private void comprobarSaveCommitInfo(CommitInfo commitInfo) {
		
		String oid = commitInfo.getIdCommit();
		
		CommitInfo result = commitInfoService.findByCommitId(oid);
		
		if(result==null) {
			commitInfoService.saveCommitInfo(commitInfo);
		}
		
	}

	public void updateRepositoryCommits(String[] info, String filename, boolean initialStarCursorFind,
			List<Commit> commitsBranch, CommitCursor commitCursor) throws IOException {

		String graphqlPayload;

		ObjectNode variables;

		String jsonData;
		JsonNode jsonNode;
		JsonNode nodes;
		JsonNode parameterNode;
		Response responseGiven;
		Iterator<JsonNode> iter;

		File file = new File(filename);

		CommitCursor commitCursorInitial = this.commitCursorService.getCommitCursorByEndCursoryHasNextPage(info[2],
				info[0]);

		String commitCursorStart = commitCursorInitial.getStartCursor().substring(0,
				commitCursorInitial.getStartCursor().indexOf(" "));


		String[] variablesPut = new String[3];
		variablesPut[0] = info[0];
		variablesPut[1] = info[1];
		variablesPut[2] = filename;

		variables = getVariables(variablesPut, info[2], commitCursor, commitCursorInitial.getStartCursor());

		graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		responseGiven = this.response.prepareResponse(graphqlPayload, this.graphqlUri, info[1]);

		jsonData = responseGiven.body().string();
		jsonNode = new ObjectMapper().readTree(jsonData);

		commitCursor = updateCommitCursor(jsonNode, info[2], info[0]);

		nodes = jsonNode.path("data").path(this.repositoryString).path("ref").path(this.targetString).path(this.historyString).path("nodes");
		iter = nodes.iterator();
		parameterNode = iter.next();

		Object [] result;
		
		Commit commit;
		CommitInfo commitInfo;
		
		while (iter.hasNext()) {

			result = introducirCommit(parameterNode, info[3]);
			
			commit = (Commit) result[0];
			commitInfo = (CommitInfo) result[1];

			if (commit != null && commitCursorStart.equals(commit.getOid())) {
				initialStarCursorFind = true;
				comprobarSaveCommitInfo(commitInfo);			
			} else if(!(initialStarCursorFind)) {			
				commitsBranch.add(commit);
				comprobarSaveCommitInfo(commitInfo);			
			}

			parameterNode = iter.next();

			if (!iter.hasNext()) {			
				result = introducirCommit(parameterNode, info[3]);			
				commit = (Commit) result[0];
				commitInfo = (CommitInfo) result[1];	
				if (commit != null && commitCursorStart.equals(commit.getOid())) {
					initialStarCursorFind = true;
					comprobarSaveCommitInfo(commitInfo);
				} 
				else if(!(initialStarCursorFind)){
					commitsBranch.add(commit);
					comprobarSaveCommitInfo(commitInfo);
				}
			}

		}

		actualizarCommitsBranch(info, initialStarCursorFind,commitsBranch,commitCursor);
		
	}


	private void actualizarCommitsBranch(String[] info, boolean initialStarCursorFind, List<Commit> commitsBranch,
			CommitCursor commitCursor) throws IOException {
		String newFilename = "";

		if (initialStarCursorFind) {

			saveCommitsBranch(commitsBranch);

		} else {
			newFilename = "src/main/resources/graphql/commits-cursor.graphql";
			updateRepositoryCommits(info, newFilename, initialStarCursorFind, commitsBranch, commitCursor);
		}
	}

	private void saveCommitsBranch(List<Commit> commitsBranch) {
		for (int j = 0; j < commitsBranch.size(); j++) {
			this.commitService.saveCommit(commitsBranch.get(j));
		}
	}

	private ObjectNode getVariables(String[] variablesPut, String name, CommitCursor commitCursor, String startCursor) {
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("repo", variablesPut[0]);
		variables.put("owner", variablesPut[1]);	
		variables.put("branch", name);
		LOG.info("Estamos en la rama: " + name);
		if (startCursor != null) {
			if (variablesPut[2].equals(this.filenameCursor) && commitCursor != null) {
				variables.put(this.cursorString, commitCursor.getEndCursor());
			} else {
				variables.put(this.cursorString, startCursor);
			}
		} else {
			LOG.info(variablesPut[2]);
			if (variablesPut[2].equals(this.filenameCursor) && commitCursor != null) {
				LOG.info("Cursor end actual: "+commitCursor.getEndCursor());
				variables.put(this.cursorString, commitCursor.getEndCursor());
			}
		}
		LOG.info(variables.toPrettyString());
		return variables;
	}

	private CommitCursor updateCommitCursor(JsonNode jsonNode, String branch, String reponame) {

		JsonNode cursorNode = jsonNode.path("data").path("repository").path("ref").path("target").path("history")
				.path("pageInfo");
		
		LOG.info(cursorNode.toString());

		boolean hasNextPage = cursorNode.get("hasNextPage").booleanValue();
		String endCursor = cursorNode.get("endCursor").textValue();
		String startCursor = cursorNode.get("startCursor").textValue();

		CommitCursor commitCursor = commitCursorService.getCommitCursorByEndCursoryHasNextPage(branch, reponame);

		if (commitCursor == null) {
			commitCursor = new CommitCursor(hasNextPage, endCursor, startCursor, branch, reponame);
			commitCursorService.saveCommitCursor(commitCursor);
		} else {
			commitCursor.setHasNextPage(hasNextPage);
			commitCursor.setStartCursor(startCursor);
			commitCursor.setEndCursor(endCursor);

			commitCursorService.updateCommitCursor(commitCursor);
		}

		LOG.info("CommitCursor->  "+commitCursor.getBranch()+" end : "+ commitCursor.getEndCursor()+ " start: "+commitCursor.getStartCursor());

		return commitCursor;

	}

	private Object [] introducirCommit(JsonNode parameterNode, String branchId) {

		Commit commit = null;
		CommitInfo commitInfo = null;
		UserGithub userGithub = null;
		
		String oid;
		
		Instant pushedDate;

		String pushedDateExtraido;
		String authoredDateExtraido;
		
		String messageHeadline;
		String message;
		int changedFiles;
				
		String authorLogin;
		String authorName;
		String authorId;
		String authorEmail;
		String authorAvatarURL;

		JsonNode nodeAuthor;
		JsonNode nodeAuthorUser;

		oid = comprobarValor(parameterNode, "oid");
		messageHeadline = comprobarValor(parameterNode, "messageHeadline");
		message = comprobarValor(parameterNode, "message");
		pushedDateExtraido = comprobarValor(parameterNode, "pushedDate");
		changedFiles = comprobarValorchangedFiles(parameterNode, "changedFiles");
		authoredDateExtraido = comprobarValor(parameterNode, "authoredDate");

		if (pushedDateExtraido == null || pushedDateExtraido.equals("")) {
			pushedDate = Instant.parse(authoredDateExtraido).plus(1, ChronoUnit.HOURS);
		} 
		else {
			pushedDate = Instant.parse(pushedDateExtraido).plus(1, ChronoUnit.HOURS);
		}

		nodeAuthor = parameterNode.path("author");
		if (nodeAuthor == null) {
			authorLogin="";
			authorName="";
			authorId="";
			authorEmail="";
			authorAvatarURL="";
			
		} else {
			authorName = comprobarValor(nodeAuthor, "name");
			authorEmail = comprobarValor(nodeAuthor, "email");
			authorAvatarURL= comprobarValor(nodeAuthor, "avatarUrl");

			nodeAuthorUser = nodeAuthor.path("user");
			if (nodeAuthorUser == null) {
				authorId = "";
				authorLogin="";
			} else {
				authorId = comprobarValor(nodeAuthorUser, "id");
				authorLogin= comprobarValor(nodeAuthorUser, "login");
			}
		}
		
		String [] authorValues = new String [5];
		authorValues[0] = authorLogin;
		authorValues[1] =authorName;
		authorValues[2] =authorId;
		authorValues[3] =authorEmail;
		authorValues[4] =authorAvatarURL;
		
		userGithub = saveAuthor(authorValues);

		LOG.info("Commit oid : " + oid);
		LOG.info("MessageHeadline: " + messageHeadline);
		LOG.info("Author" + userGithub.toString());

		commit = new Commit(oid, pushedDate, userGithub.getId(), branchId);
		
		commitInfo = new CommitInfo(oid, messageHeadline, message, changedFiles);
		
		Object [] result = new Object[2];
		
		result[0] = commit;
		result[1] = commitInfo;
		
		return result;
	}

	private UserGithub saveAuthor(String [] authorValues) {
		
		String authorLogin = authorValues[0];
		String authorName  = authorValues[1];
		String authorId = authorValues[2];
		String authorEmail = authorValues[3];
		String authorAvatarURL = authorValues[4];
		
		UserGithub userGithub;
		
		if( authorLogin != null && !authorLogin.equals("")) {
			userGithub = userGithubService.findByLogin(authorLogin);
			
			if(userGithub==null) {
				userGithub = new UserGithub(authorLogin, authorEmail, authorAvatarURL, authorId, authorName);
				userGithubService.saveUserGithub(userGithub);
			}
		
		}
		
		else if( authorName != null && !authorName.equals("")) {
			userGithub = userGithubService.findByName(authorName);
			
			if(userGithub==null) {
				userGithub = new UserGithub(authorLogin, authorEmail, authorAvatarURL, authorId, authorName);
				userGithubService.saveUserGithub(userGithub);
			}
		
		}
		
		userGithub = userGithubService.findByLogin(authorLogin);
		if(userGithub==null) {
			userGithub = userGithubService.findByName(authorName);
		}
		
		return userGithub;
		
	}

	private int comprobarValorchangedFiles(JsonNode parameterNode, String value) {
		if (parameterNode.get(value) == null) {
			return 0;
		} else {
			return parameterNode.get(value).intValue();
		}

	}

	private String comprobarValor(JsonNode parameterNode, String textValue) {

		if (parameterNode.get(textValue) == null) {
			return "";
		} else {
			return parameterNode.get(textValue).textValue();
		}

	}

}