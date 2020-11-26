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
import edu.uclm.esi.devopsmetrics.models.UserGithub;
import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitService;

@Service
@Scope("singleton")
public class GithubOperations {

	private static final Log LOG = LogFactory.getLog(GithubOperations.class);

	private final CommitService commitService;
	private final BranchService branchService;
	private final BranchesGithub branchesGithub;
	private final CommitsGithub commitsGithub;

	/**
	 * @author FcoCrespo
	 */
	public GithubOperations(final CommitService commitService, final BranchService branchService,
			final BranchesGithub branchesGithub, final CommitsGithub commitsGithub) {

		this.commitService = commitService;
		this.branchService = branchService;
		this.branchesGithub = branchesGithub;
		this.commitsGithub = commitsGithub;

	}

	public String getBranches(String reponame, String owner) {

		try {
			this.branchesGithub.getBranches(reponame, owner);
			List<Branch> listBranches = this.branchService.getBranchesByRepository(reponame, true);
			Collections.sort(listBranches);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(listBranches);
		} catch (JsonProcessingException e) {
			return null;
		}

	}

	public void getCommits(String reponame, String owner) throws IOException {

		getBranches(reponame, owner);

		List<Branch> branches = this.branchService.getBranchesByRepository(reponame, false);

		Commit commitDeRepo;

		CommitCursor commitCursor = null;

		boolean seguir = true;
		int j = 0;
		while (seguir && j < branches.size()) {
			commitDeRepo = commitService.getCommitByBranch(branches.get(j).getIdGithub());

			if (commitDeRepo != null) {
				seguir = false;
			} else {
				j++;
			}

		}

		String filename;
		boolean initialStarCursorFind = false;

		String[] info = new String[5];
		info[0] = reponame;
		info[1] = owner;

		if (seguir) {
			LOG.info("INTRODUCIENDO NUEVOS COMMITS");
			filename = "src/main/resources/graphql/commits.graphql";
			for (int i = 0; i < branches.size(); i++) {
				info[2] = branches.get(i).getName();
				info[3] = branches.get(i).getIdGithub();
				this.commitsGithub.getNewRepositoryCommits(info, filename, commitCursor);
			}
		} else {
			LOG.info("ACTUALIZANDO COMMITS");
			filename = "src/main/resources/graphql/commits-cursor-before.graphql";
			List<Commit> commitsBranch = new ArrayList<Commit>();

			for (int i = 0; i < branches.size(); i++) {
				info[2] = branches.get(i).getName();
				info[3] = branches.get(i).getIdGithub();
				this.commitsGithub.updateRepositoryCommits(info, filename, initialStarCursorFind, commitsBranch,
						commitCursor);
			}
		}

	}

	public void deleteCommits(String branchId) {
		this.commitService.deleteCommits(branchId);
	}

	public void getFirstCommitByBranch(String reponame) throws IOException {

		List<Branch>branchesRepo = this.branchService.getBranchesByRepository(reponame, false);
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://localhost:5050/branchesorder?reponame=" + branchesRepo.get(0).getRepository());
		LOG.info("Request Type: " + httpget.getMethod());

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		String jsonData = EntityUtils.toString(entity, "UTF-8");
		JsonNode jsonNode = new ObjectMapper().readTree(jsonData);

		Iterator<JsonNode> iter;
		iter = jsonNode.iterator();

		JsonNode parameterNode;
		parameterNode = iter.next();

		String branchname;
		String commitoid;

		List<Commit> firstCommitByBranch = new ArrayList<Commit>();

		List<String> commitOidRequest = new ArrayList<String>();
		List<String> branchesNamesRequest = new ArrayList<String>();

		while (iter.hasNext()) {
			branchname = parameterNode.get("branchname").textValue();
			commitoid = parameterNode.get("commit").textValue();
			if (!commitoid.equals("empty")) {
				commitOidRequest.add(commitoid);
				branchesNamesRequest.add(branchname);
			}

			parameterNode = iter.next();
			if (!iter.hasNext()) {
				branchname = parameterNode.get("branchname").textValue();
				commitoid = parameterNode.get("commit").textValue();

				if (!commitoid.equals("empty")) {
					commitOidRequest.add(commitoid);
					branchesNamesRequest.add(branchname);
				}

			}
		}

		List<Branch> branchesRequest = getBranches(branchesNamesRequest, reponame);

		boolean seguir = true;
		int index = 0;
		for (int i = 0; i < branchesRequest.size() && seguir; i++) {
			if (branchesRequest.get(i).getName().equals("master")) {
				index = i;
				seguir = false;
			}
		}
		branchesRequest.remove(index);
		commitOidRequest.remove(index);

		for (int i = 0; i < commitOidRequest.size(); i++) {
			firstCommitByBranch.add(this.commitService.getCommitByOidyBranch(commitOidRequest.get(i),
					branchesRequest.get(i).getIdGithub()));
		}

		Collections.sort(firstCommitByBranch);

		Branch branch;
		for (int i = 0; i < firstCommitByBranch.size(); i++) {
			branch = this.branchService.findById(firstCommitByBranch.get(i).getBranchId());
			LOG.info(firstCommitByBranch.get(i).getOid() + " --- " + firstCommitByBranch.get(i).getBranchId() + " --- "
					+ branch.getName());
			branch.setOrder(i + 1);
			this.branchService.saveBranch(branch);
		}

		branch = this.branchService.getBranchByRepositoryyName(reponame, "master");
		branch.setOrder(0);
		this.branchService.saveBranch(branch);

		httpclient.close();
	}

	private List<Branch> getBranches(List<String> branchesNamesRequest, String reponame) {
		List<Branch> branchesRequest = new ArrayList<Branch>();
		for (int i = 0; i < branchesNamesRequest.size(); i++) {
			branchesRequest.add(this.branchService.getBranchByRepositoryyName(reponame, branchesNamesRequest.get(i)));
		}
		return branchesRequest;
	}

	public String getCommitsFromRepositoryBranch(String reponame, String name) {

		Branch branch = this.branchService.getBranchByRepositoryyName(reponame, name);
		
		List<Commit> commits;
		
		if(branch.getOrder()==0 || branch.getOrder()==1) {
			commits = this.commitService.getAllCommitsByBranch(branch.getIdGithub());
			Collections.sort(commits);
		}
		else {
			Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			List<Commit> commitsQuery = this.commitService.getAllCommitsByBranch(branch.getIdGithub());
			List<Commit> commitsBefore = this.commitService.getAllCommitsByBranch(branchBefore.getIdGithub());
			
			commits = filterCommits(commitsQuery, commitsBefore);
			Collections.sort(commits);
		}

		return getInfoCommits(commits, branch);
	}

	private List<Commit> filterCommits(List<Commit> commitsQuery, List<Commit> commitsBefore) {
		boolean seguir=true;
	      List <Commit> commits =  new ArrayList<Commit>();
	      
	      
	      for(int i = 0; i<commitsQuery.size(); i++) {
	    	  for(int j = 0; j<commitsBefore.size()&&seguir; j++) {
	    		  if (commitsQuery.get(i).getOid().equals(commitsBefore.get(j).getOid())){
	    			  seguir=false;
	    		  }
	    	  }
	    	  if(seguir) {
	    		  commits.add(commitsQuery.get(i));
	    	  }
	    	  seguir=true;
	      }
	      
	      Collections.sort(commits);
	      
	      return commits;
	}

	private Map<String, UserGithub> getMapUsersGithub(List<UserGithub> usersgithub) {
		Map<String, UserGithub> map = new HashMap<String, UserGithub>();
		for (UserGithub i : usersgithub)
			map.put(i.getId(), i);
		return map;
	}

	private Map<String, CommitInfo> getMapCommitsInfo(List<CommitInfo> commitsInfo) {
		Map<String, CommitInfo> map = new HashMap<String, CommitInfo>();
		for (CommitInfo i : commitsInfo)
			map.put(i.getIdCommit(), i);
		return map;
	}
	
	public String getAllByBranchBeginEndDate(String reponame, String name, Instant beginDateInstant,
			Instant endDateInstant) {
		Branch branch = this.branchService.getBranchByRepositoryyName(reponame, name);
		List<Commit> commits;
		
		
		if(branch.getOrder()==0 || branch.getOrder()==1) {
			commits = this.commitService.getAllByBranchBeginEndDate(branch.getIdGithub(), beginDateInstant, endDateInstant);
			Collections.sort(commits);
		}
		else {
			Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			List<Commit> commitsQuery = this.commitService.getAllByBranchBeginEndDate(branch.getIdGithub(), beginDateInstant, endDateInstant);
			List<Commit> commitsBefore = this.commitService.getAllByBranchBeginEndDate(branchBefore.getIdGithub(), beginDateInstant, endDateInstant);
			
			commits = filterCommits(commitsQuery, commitsBefore);
			Collections.sort(commits);
		}
		
		return getInfoCommits(commits, branch);
	}
	
	public String getAllByBranchBeginEndDateByAuthor(String reponame, String name, Instant beginDateInstant,
			Instant endDateInstant, String authorName) {
		Branch branch = this.branchService.getBranchByRepositoryyName(reponame, name);
		UserGithub userGithub = this.commitsGithub.getUserGithubByName(authorName);
		List<Commit> commits;
		
		
		if(branch.getOrder()==0 || branch.getOrder()==1) {
			commits = this.commitService.getAllByBranchBeginEndDateByAuthor(branch.getIdGithub(), beginDateInstant, endDateInstant, userGithub.getId());
			Collections.sort(commits);
		}
		else {
			Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			List<Commit> commitsQuery = this.commitService.getAllByBranchBeginEndDateByAuthor(branch.getIdGithub(), beginDateInstant, endDateInstant, userGithub.getId());
			List<Commit> commitsBefore = this.commitService.getAllByBranchBeginEndDateByAuthor(branchBefore.getIdGithub(), beginDateInstant, endDateInstant, userGithub.getId());
			
			commits = filterCommits(commitsQuery, commitsBefore);
			Collections.sort(commits);
		}
		
		return getInfoCommits(commits, branch);
	}

	public String getCommitsByBranchAndAuthorName(String reponame, String name, String authorName) {
		Branch branch = this.branchService.getBranchByRepositoryyName(reponame, name);
		UserGithub userGithub = this.commitsGithub.getUserGithubByName(authorName);
		List<Commit> commits;
		
		if(branch.getOrder()==0 || branch.getOrder()==1) {
			commits = this.commitService.getAllCommitsByBranchAndAuthor(branch.getIdGithub(), userGithub.getId());
			Collections.sort(commits);
		}
		else {
			Branch branchBefore = branchService.getBeforeBranchByOrder(reponame, branch.getOrder());
			List<Commit> commitsQuery = this.commitService.getAllCommitsByBranchAndAuthor(branch.getIdGithub(), userGithub.getId());
			List<Commit> commitsBefore = this.commitService.getAllCommitsByBranchAndAuthor(branchBefore.getIdGithub(), userGithub.getId());
			
			commits = filterCommits(commitsQuery, commitsBefore);
			Collections.sort(commits);
		}
		
		return getInfoCommits(commits, branch);
	}

	private String getInfoCommits(List<Commit> commits, Branch branch) {
		UserGithub userGithub = null;

		CommitInfo commitInfo = null;

		JSONArray array = new JSONArray();
		JSONObject json;
		List<UserGithub> usersgithub = this.commitsGithub.getUsersGithub();
		List<CommitInfo> commitsInfo = this.commitsGithub.getCommitsInfo();

		Map<String, CommitInfo> mapCommitsInfo = getMapCommitsInfo(commitsInfo);
		Map<String, UserGithub> mapUsersGithub = getMapUsersGithub(usersgithub);
		
		for(int i=0; i<commits.size(); i++) {
			json = new JSONObject();
			
			json.put("id", commits.get(i).getId());
			json.put("oid", commits.get(i).getOid());
			json.put("pushedDate", commits.get(i).getPushedDate());
			
			userGithub = mapUsersGithub.get(commits.get(i).getUsergithub());
			
			json.put("authorName", userGithub.getName());

			commitInfo = mapCommitsInfo.get(commits.get(i).getOid());

			if (commitInfo != null) {
				json.put("messageHeadline", commitInfo.getMessageHeadline());
				json.put("message", commitInfo.getMessage());
				json.put("changedFiles", commitInfo.getChangedFiles());
			}

			json.put("branch", branch.getName());
			json.put("repository", branch.getRepository());
			
			array.put(json);
		}	
		
		commits.clear();
		commitsInfo.clear();
		mapCommitsInfo.clear();
		mapUsersGithub.clear();

		return array.toString();
	}

	

}
