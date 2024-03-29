package edu.uclm.esi.devopsmetrics.domain;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.entities.Issue;
import edu.uclm.esi.devopsmetrics.entities.IssueAssignee;
import edu.uclm.esi.devopsmetrics.entities.IssueCursor;
import edu.uclm.esi.devopsmetrics.entities.IssueRepo;
import edu.uclm.esi.devopsmetrics.entities.UserGithub;
import edu.uclm.esi.devopsmetrics.services.IssueAssigneeService;
import edu.uclm.esi.devopsmetrics.services.IssueRepoService;
import edu.uclm.esi.devopsmetrics.services.IssueService;
import edu.uclm.esi.devopsmetrics.services.UserGithubService;
import edu.uclm.esi.devopsmetrics.utilities.DateUtils;

/**
*
* @author FcoCrespo
* 
*/
@Service
public class IssueOperations {

	private static final Log LOG = LogFactory.getLog(IssueOperations.class);

	private final IssuesGithub issuesGithub;
	private final IssueServices issueServices;
	private final IssueRepoService issueRepoService;
	private final IssueAssigneeService issueAssigneeService;
	private final IssueService issueService;
	private final UserGithubServices userGithubServices;
	private final UserGithubService userGithubService;
	private String loginStr;
	private String emailStr;
	private String avatarURLStr;
	private String repositoryStr;
	private String titleStr;
	private String authorStr;
	private String createdAtStr;
	private String closedAtStr;
	private String assigneesStr;
	private String stateStr;
	
	/**
	 * @author FcoCrespo
	 */
	public IssueOperations(final IssuesGithub issuesGithub, final IssueServices issueServices,
			final UserGithubServices userGithubServices) {
		this.issuesGithub = issuesGithub;
		this.issueServices = issueServices;
		this.issueRepoService = this.issueServices.getIssueRepoService();
		this.issueAssigneeService = this.issueServices.getIssueAssigneeService();
		this.issueService = this.issueServices.getIssueService();
		this.userGithubServices = userGithubServices;
		this.userGithubService = this.userGithubServices.getUserGithubService();
		this.loginStr = "login";
		this.emailStr = "email";
		this.avatarURLStr = "avatarURL";
		this.repositoryStr = "repository";
		this.titleStr = "title";
		this.createdAtStr = "createdAt";
		this.closedAtStr = "closedAt";
		this.assigneesStr = "assignees";
		this.authorStr = "author";
		this.stateStr = "state";
	}

	public String getIssues(String repository, String owner) throws IOException {

		IssueRepo issueDeRepo = null;

		IssueCursor issueCursor = null;

		boolean seguir = true;

		issueDeRepo = this.issueRepoService.getOneByRepoyOwner(repository, owner);

		if (issueDeRepo == null) {
			seguir = true;
		} else {
			seguir = false;
		}

		String filename;
		boolean initialStartCursorFind = false;

		String[] info = new String[3];
		info[0] = repository;
		info[1] = owner;

		if (seguir) {
			LOG.info("INTRODUCIENDO NUEVOS ISSUES DEL REPOSITORIO");
			filename = "src/main/resources/graphql/issues.graphql";

			this.issuesGithub.getNewRepositoryIssues(info, filename, issueCursor);

		} else {
			LOG.info("ACTUALIZANDO ISSUES DEL REPOSITORIO");
			filename = "src/main/resources/graphql/issues-cursor-before.graphql";
			info[2] = filename;

			List<Issue> issuesList = new ArrayList<Issue>();
			List<IssueRepo> issuesRepoList = new ArrayList<IssueRepo>();

			this.issuesGithub.updateRepositoryIssues(info, initialStartCursorFind, issuesList, issuesRepoList,
					issueCursor);

		}
		
		return "Ok";

	}

	public String actualizarValores(String repository, String owner) throws IOException {

		String filename = "src/main/resources/graphql/issues.graphql";

		String[] info = new String[3];
		info[0] = repository;
		info[1] = owner;

		IssueCursor issueCursor = null;

		this.issuesGithub.actualizarValores(info, filename, issueCursor);
		
		return "OK.";
	}

	public String getIssuesRepository(String reponame, String owner) {

		UserGithub userGithub = null;

		JSONArray array = new JSONArray();
		JSONObject json;
		JSONArray jsonAssignees;
		JSONObject jsonAuthor;

		List<UserGithub> usersgithub = this.userGithubService.findAll();
		List<Issue> issues = this.issueService.findAll();

		List<IssueAssignee> issuesAssignee;

		List<IssueRepo> issueRepoList = this.issueRepoService.getByRepoyOwner(reponame, owner);

		Map<String, Issue> mapIssues = getMapIssues(issues);
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);

		for (int i = 0; i < issueRepoList.size(); i++) {

			json = new JSONObject();
			jsonAuthor = new JSONObject();

			issuesAssignee = this.issueAssigneeService.getAllByIdIssue(issueRepoList.get(i).getId());

			userGithub = mapUsersGithub.get(issueRepoList.get(i).getAuthor());

			jsonAuthor.put("id", userGithub.getId());
			jsonAuthor.put(this.loginStr, userGithub.getLogin());
			jsonAuthor.put("name", userGithub.getName());
			jsonAuthor.put(this.emailStr, userGithub.getEmail());
			jsonAuthor.put(this.avatarURLStr, userGithub.getAvatarURL());

			json.put(this.repositoryStr, issueRepoList.get(i).getRepository());
			json.put(this.authorStr, jsonAuthor);

			json.put("id", mapIssues.get(issueRepoList.get(i).getId()).getId());
			json.put(this.titleStr, mapIssues.get(issueRepoList.get(i).getId()).getTitle());
			json.put(this.createdAtStr, mapIssues.get(issueRepoList.get(i).getId()).getCreatedAt());
			json.put(this.closedAtStr, mapIssues.get(issueRepoList.get(i).getId()).getClosedAt());
			json.put(this.stateStr, mapIssues.get(issueRepoList.get(i).getId()).getState());

			jsonAssignees = getJSONAssignees(issuesAssignee, mapUsersGithub);

			json.put(this.assigneesStr, jsonAssignees);

			array.put(json);

		}

		return array.toString();

	}
	
	public String getIssuesRepositoryByAssignee(String reponame, String owner, String idUser) {

		UserGithub userGithub = null;

		JSONArray array = new JSONArray();
		JSONObject json;
		JSONArray jsonAssignees;
		JSONObject jsonAuthor;

		List<UserGithub> usersgithub = this.userGithubService.findAll();
		List<Issue> issues = this.issueService.findAll();

		List<IssueAssignee> issuesAssignee;

		List<IssueRepo> issueRepoList = this.issueRepoService.getByRepoyOwner(reponame, owner);

		Map<String, Issue> mapIssues = getMapIssues(issues);
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);
		
		boolean esAssignee = false;

		for (int i = 0; i < issueRepoList.size(); i++) {

			json = new JSONObject();
			jsonAuthor = new JSONObject();

			issuesAssignee = this.issueAssigneeService.getAllByIdIssue(issueRepoList.get(i).getId());
			
		  
			for(int j = 0; j<issuesAssignee.size()&&!esAssignee; j++) {
				if(issuesAssignee.get(j).getUsergithub().equals(idUser)) {
					esAssignee=true;
				}
			}
			if(esAssignee) {
				userGithub = mapUsersGithub.get(issueRepoList.get(i).getAuthor());

				jsonAuthor.put("id", userGithub.getId());
				jsonAuthor.put(this.loginStr, userGithub.getLogin());
				jsonAuthor.put("name", userGithub.getName());
				jsonAuthor.put(this.emailStr, userGithub.getEmail());
				jsonAuthor.put(this.avatarURLStr, userGithub.getAvatarURL());

				json.put(this.repositoryStr, issueRepoList.get(i).getRepository());
				json.put(this.authorStr, jsonAuthor);

				json.put("id", mapIssues.get(issueRepoList.get(i).getId()).getId());
				json.put(this.titleStr, mapIssues.get(issueRepoList.get(i).getId()).getTitle());
				json.put(this.createdAtStr, mapIssues.get(issueRepoList.get(i).getId()).getCreatedAt());
				json.put(this.closedAtStr, mapIssues.get(issueRepoList.get(i).getId()).getClosedAt());
				json.put(this.stateStr, mapIssues.get(issueRepoList.get(i).getId()).getState());

				jsonAssignees = getJSONAssignees(issuesAssignee, mapUsersGithub);

				json.put(this.assigneesStr, jsonAssignees);

				array.put(json);
			}
			
			esAssignee = false;

		}

		return array.toString();

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
	
	private Map<String, IssueRepo> getMapIssuesRepo(List<IssueRepo> issueRepoList, Map<String, Issue> mapIssues) {
		Map<String, IssueRepo> mapMapIssuesRepo = new HashMap<String, IssueRepo>();
		Issue issue;
		for (IssueRepo i : issueRepoList) {
			issue = mapIssues.get(i.getId());
			if (issue != null && issue.getId().equals(i.getId())) {
				mapMapIssuesRepo.put(i.getId(), i);
			}
		}
		return mapMapIssuesRepo;
	}
	

	public String getIssuesRepositoryDatesClosed(String reponame, String owner, String begindate, String enddate) {
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];

		List<Issue> issues = this.issueService.getAllByClosedBetweenBeginEndDate(beginDateInstant, endDateInstant);
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDates(mapIssues, reponame, owner);
	}

	public String getIssuesRepositoryDatesOpened(String reponame, String owner, String begindate, String enddate) {
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];

		List<Issue> issues = this.issueService.getAllByOpenBetweenBeginEndDate(beginDateInstant, endDateInstant);
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDates(mapIssues, reponame, owner);
	}

	public String getIssuesRepositoryDatesCreation(String reponame, String owner, String begindate, String enddate) {
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];
		
		List<Issue> issues = this.issueService.getAllByCreationBetweenBeginEndDate(beginDateInstant, endDateInstant);
		
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDates(mapIssues, reponame, owner);
		

	}
	
	public String getIssuesRepositoryDatesCreationUser(String reponame, String owner, String begindate, String enddate,
			String idUser) {
		
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];
		
		List<Issue> issues = this.issueService.getAllByCreationBetweenBeginEndDate(beginDateInstant, endDateInstant);
		
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDatesUser(mapIssues, reponame, owner, idUser);
	}
	
	public String getIssuesRepositoryDatesClosedUser(String reponame, String owner, String begindate, String enddate,
			String idUser) {
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];

		List<Issue> issues = this.issueService.getAllByClosedBetweenBeginEndDate(beginDateInstant, endDateInstant);
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDatesUser(mapIssues, reponame, owner, idUser);
	}
	
	public String getIssuesRepositoryDatesOpenedUser(String reponame, String owner, String begindate, String enddate,
			String idUser) {
		Instant[] dates = DateUtils.getDatesInstant(begindate, enddate);

		Instant beginDateInstant = dates[0];
		Instant endDateInstant = dates[1];

		List<Issue> issues = this.issueService.getAllByOpenBetweenBeginEndDate(beginDateInstant, endDateInstant);
		Map<String, Issue> mapIssues = getMapIssues(issues);

		return obtenerIssuesBetweenDatesUser(mapIssues, reponame, owner, idUser);
	}

	private String obtenerIssuesBetweenDates(Map<String, Issue> mapIssues, String reponame, String owner) {

		UserGithub userGithub = null;

		JSONArray array = new JSONArray();
		JSONObject json;
		JSONObject jsonAuthor;

		List<UserGithub> usersgithub = this.userGithubService.findAll();
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);
		List<IssueRepo> issueRepoList = this.issueRepoService.getByRepoyOwner(reponame, owner);
		Map<String, IssueRepo> mapIssuesRepo = getMapIssuesRepo(issueRepoList, mapIssues);

		List<IssueAssignee> issuesAssignee;

		Set<Entry<String, IssueRepo>> entrySet = mapIssuesRepo.entrySet();

		IssueRepo issueRepo;

		for (Entry<String, IssueRepo> entry : entrySet) {
			json = new JSONObject();
			jsonAuthor = new JSONObject();

			issueRepo = mapIssuesRepo.get(entry.getKey());

			issuesAssignee = this.issueAssigneeService.getAllByIdIssue(issueRepo.getId());

			userGithub = mapUsersGithub.get(issueRepo.getAuthor());

			jsonAuthor.put("id", userGithub.getId());
			jsonAuthor.put(this.loginStr, userGithub.getLogin());
			jsonAuthor.put("name", userGithub.getName());
			jsonAuthor.put(this.emailStr, userGithub.getEmail());
			jsonAuthor.put(this.avatarURLStr, userGithub.getAvatarURL());

			json.put(this.repositoryStr, issueRepo.getRepository());
			json.put(this.authorStr, jsonAuthor);

			json.put(this.titleStr, mapIssues.get(issueRepo.getId()).getTitle());
			json.put(this.createdAtStr, mapIssues.get(issueRepo.getId()).getCreatedAt());
			json.put(this.closedAtStr, mapIssues.get(issueRepo.getId()).getClosedAt());
			json.put(this.stateStr, mapIssues.get(issueRepo.getId()).getState());
			json.put("id", mapIssues.get(issueRepo.getId()).getId());

			JSONArray jsonAssignees = getJSONAssignees(issuesAssignee, mapUsersGithub);

			json.put(this.assigneesStr, jsonAssignees);

			array.put(json);
			
		}

		return array.toString();
	}
	
	private String obtenerIssuesBetweenDatesUser(Map<String, Issue> mapIssues, String reponame, String owner,
			String idUser) {
		
		UserGithub userGithub = null;

		JSONArray array = new JSONArray();
		JSONObject json;
		JSONObject jsonAuthor;

		List<UserGithub> usersgithub = this.userGithubService.findAll();
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);
		List<IssueRepo> issueRepoList = this.issueRepoService.getByRepoyOwner(reponame, owner);
		Map<String, IssueRepo> mapIssuesRepo = getMapIssuesRepo(issueRepoList, mapIssues);

		List<IssueAssignee> issuesAssignee;

		Set<Entry<String, IssueRepo>> entrySet = mapIssuesRepo.entrySet();

		IssueRepo issueRepo;

		for (Entry<String, IssueRepo> entry : entrySet) {
			
			
			json = new JSONObject();
			jsonAuthor = new JSONObject();

			issueRepo = mapIssuesRepo.get(entry.getKey());

			issuesAssignee = this.issueAssigneeService.getAllByIdIssue(issueRepo.getId());
			
			if(comprobarExisteAssignee(issuesAssignee, idUser)) {
				
				JSONArray jsonAssignees = getJSONAssignees(issuesAssignee, mapUsersGithub);
				
				userGithub = mapUsersGithub.get(issueRepo.getAuthor());

				jsonAuthor.put("id", userGithub.getId());
				jsonAuthor.put(this.loginStr, userGithub.getLogin());
				jsonAuthor.put("name", userGithub.getName());
				jsonAuthor.put(this.emailStr, userGithub.getEmail());
				jsonAuthor.put(this.avatarURLStr, userGithub.getAvatarURL());

				json.put(this.repositoryStr, issueRepo.getRepository());
				json.put(this.authorStr, jsonAuthor);

				json.put(this.titleStr, mapIssues.get(issueRepo.getId()).getTitle());
				json.put(this.createdAtStr, mapIssues.get(issueRepo.getId()).getCreatedAt());
				json.put(this.closedAtStr, mapIssues.get(issueRepo.getId()).getClosedAt());
				json.put(this.stateStr, mapIssues.get(issueRepo.getId()).getState());
				json.put("id", mapIssues.get(issueRepo.getId()).getId());

				

				json.put(this.assigneesStr, jsonAssignees);

				array.put(json);
			}
			
		}

		return array.toString();
	}

	private boolean comprobarExisteAssignee(List<IssueAssignee> issuesAssignee, String idUser) {
		 
		boolean existe = false; 

		for (int i = 0; i < issuesAssignee.size()&&!existe; i++) {
			
			if(issuesAssignee.get(i).getUsergithub().equals(idUser)) {
				existe=true;
			}
			
		}
		return existe;
	}

	
	private JSONArray getJSONAssignees(List<IssueAssignee> issuesAssignee, Map<String, UserGithub> mapUsersGithub) {

		JSONArray array = new JSONArray();
		JSONObject json;

		UserGithub userGithub = null;

		for (int i = 0; i < issuesAssignee.size(); i++) {

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



}