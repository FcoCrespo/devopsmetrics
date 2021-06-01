package edu.uclm.esi.devopsmetrics.domain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import edu.uclm.esi.devopsmetrics.models.Issue;
import edu.uclm.esi.devopsmetrics.models.IssueAssignee;
import edu.uclm.esi.devopsmetrics.models.IssueCursor;
import edu.uclm.esi.devopsmetrics.models.IssueRepo;
import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.services.IssueAssigneeService;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;
import edu.uclm.esi.devopsmetrics.services.IssueService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;

@Service
@Scope("singleton")
public class IssueOperations {
	
	private static final Log LOG = LogFactory.getLog(IssueOperations.class);

	private IssueRepoService issueRepoService;
	private IssueAssigneeService issueAssigneeService;
	private IssueService issueService;
	private UserGithubService userGithubService;
	
	/**
	 * @author FcoCrespo
	 */
	private IssueOperations() {
		
	}
	
	private static class IssueOperationsHolder {
		static IssueOperations singleton=new IssueOperations();
	}
	
	public static IssueOperations get() {
		return IssueOperationsHolder.singleton;
	}
	
	public void getIssues(String repository, String owner) throws IOException {


		IssueRepo issueDeRepo = null;

		IssueCursor issueCursor = null;

		boolean seguir = true;
		
		issueDeRepo = issueRepoService.getOneByRepoyOwner(repository, owner);
		
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
			LOG.info("INTRODUCIENDO NUEVOS ISSUES DEL REPOSITORIO");
			filename = "src/main/resources/graphql/issues.graphql";
			
			IssuesGithub.get().getNewRepositoryIssues(info, filename, issueCursor);
		
		} else {
			LOG.info("ACTUALIZANDO ISSUES DEL REPOSITORIO");
			filename = "src/main/resources/graphql/issues-cursor-before.graphql";
			info[2]=filename;
			
			List<Issue>issuesList = new ArrayList<Issue>();
			List<IssueRepo>issuesRepoList = new ArrayList<IssueRepo>();
			
			IssuesGithub.get().updateRepositoryIssues(info, initialStartCursorFind, issuesList, issuesRepoList, issueCursor);
			
		}
		
		

	}
	
	public void actualizarValores(String repository, String owner) throws IOException {
		
		String filename = "src/main/resources/graphql/issues.graphql";


		String[] info = new String[3];
		info[0] = repository;
		info[1] = owner;
		
		IssueCursor issueCursor = null;
		
		IssuesGithub.get().actualizarValores(info, filename, issueCursor);
	}

	public String getIssuesRepository(String reponame, String owner) {
		
		UserGithub userGithub = null;
		
		JSONArray array = new JSONArray();
		JSONObject json;
		JSONArray jsonAssignees;
		JSONObject jsonAuthor;
		
		List <UserGithub> usersgithub = userGithubService.findAll();
		List <Issue> issues = issueService.findAll();
		List <IssueAssignee> issuesAssignee = new ArrayList<>();
		
		List <IssueRepo> issueRepoList = issueRepoService.getByRepoyOwner(reponame, owner);
		
		Map<String, Issue> mapIssues = getMapIssues(issues);
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);		
		
		for(int i=0; i<issueRepoList.size(); i++) {
			
			json = new JSONObject();
			jsonAuthor = new JSONObject();
			
			issuesAssignee = issueAssigneeService.getAllByIdIssue(issueRepoList.get(i).getId());
			
			userGithub = mapUsersGithub.get(issueRepoList.get(i).getAuthor());
			
			jsonAuthor.put("id", userGithub.getId());
			jsonAuthor.put("login", userGithub.getLogin());
			jsonAuthor.put("name", userGithub.getName());
			jsonAuthor.put("email", userGithub.getEmail());
			jsonAuthor.put("avatarURL", userGithub.getAvatarURL());
			
			json.put("repository", issueRepoList.get(i).getRepository());
			json.put("author", jsonAuthor);
			
			json.put("title", mapIssues.get(issueRepoList.get(i).getId()).getTitle());
			json.put("body", mapIssues.get(issueRepoList.get(i).getId()).getBody());
			json.put("createdAt", mapIssues.get(issueRepoList.get(i).getId()).getCreatedAt());
			json.put("closedAt", mapIssues.get(issueRepoList.get(i).getId()).getClosedAt());
			json.put("state", mapIssues.get(issueRepoList.get(i).getId()).getState());
			
			jsonAssignees = getJSONAssignees(issuesAssignee, mapUsersGithub);
			
			json.put("assignees", jsonAssignees);	
			
			array.put(json);
		}
		
		usersgithub.clear();
		issues.clear();
		issuesAssignee.clear();
		issueRepoList.clear();
		mapIssues.clear();
		mapUsersGithub.clear();
		
		return array.toString();
		
	}
	
	private JSONArray getJSONAssignees(List<IssueAssignee> issuesAssignee, Map<String, UserGithub> mapUsersGithub) {
		
		JSONArray array = new JSONArray();
		JSONObject json;
		
		UserGithub userGithub = null;
		
		for(int i=0; i<issuesAssignee.size(); i++) {
			
			json = new JSONObject();
			
			userGithub = mapUsersGithub.get(issuesAssignee.get(i).getUsergithub());
			
			json.put("id", userGithub.getId());
			json.put("login", userGithub.getLogin());
			json.put("name", userGithub.getName());
			json.put("email", userGithub.getEmail());
			json.put("avatarURL", userGithub.getAvatarURL());
			
			array.put(json);
		}
		
		return array;
	}

	private Map<String, UserGithub> getMapUsersGithub(List<UserGithub> usersgithub) {
		Map<String, UserGithub> map = new HashMap<String, UserGithub>();
		for (UserGithub i : usersgithub)
			map.put(i.getId(), i);
		return map;
	}
	
	private Map<String, Issue> getMapIssues(List<Issue> issues) {
		Map<String, Issue> map = new HashMap<String, Issue>();
		for (Issue i : issues)
			map.put(i.getId(), i);
		return map;
	}

}
