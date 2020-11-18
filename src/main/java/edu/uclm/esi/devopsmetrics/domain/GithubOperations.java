package edu.uclm.esi.devopsmetrics.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
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
		public GithubOperations (final CommitService commitService, final BranchService branchService, 
				final BranchesGithub branchesGithub, final CommitsGithub commitsGithub) {
			
			this.commitService = commitService;
			this.branchService =  branchService;
			this.branchesGithub = branchesGithub;
			this.commitsGithub = commitsGithub;

		}

	public String getBranches(String reponame, String owner) {
		
		try {
			this.branchesGithub.getBranches(reponame, owner);
			List <Branch> listBranches = this.branchService.getBranchesByRepository(reponame, true);
			Collections.sort(listBranches);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(listBranches);
		} catch (JsonProcessingException e) {
			return null;
		}
		
	}

	public void getCommits(String reponame, String owner) throws IOException {
		
		getBranches(reponame, owner);
		
		List<Branch>branches = this.branchService.getBranchesByRepository(reponame, false);
		
		Commit commitDeRepo;
		
		CommitCursor commitCursor= null;	
		
		boolean seguir=true;
		int j = 0;
		while(seguir && j<branches.size()) {
			commitDeRepo = commitService.getCommitByBranch(branches.get(j).getIdGithub());
			
			if(commitDeRepo!=null) {
				seguir=false;
			}
			else {
				j++;
			}
			
		}

		String filename;
		boolean initialStarCursorFind = false;
		
		String [] info = new String[5];
		info[0] = reponame;
		info[1] = owner; 	
				
  		
  		if(seguir) {
  			LOG.info("INTRODUCIENDO NUEVOS COMMITS");
  			filename = "src/main/resources/graphql/commits.graphql";
  			for(int i = 0; i< branches.size(); i++) {
  				info[2]=branches.get(i).getName();
  				info[3]=branches.get(i).getIdGithub();
  	  			this.commitsGithub.getNewRepositoryCommits(info, filename, commitCursor);
  			}
  		}
  		else {
  			LOG.info("ACTUALIZANDO COMMITS");
  			filename = "src/main/resources/graphql/commits-cursor-before.graphql";		
  			List<Commit> commitsBranch = new ArrayList<Commit>();
  			
  			
  			for(int i = 0; i< branches.size(); i++) {
  				info[2]=branches.get(i).getName();
  				info[3]=branches.get(i).getIdGithub();
  	  			this.commitsGithub.updateRepositoryCommits(info, filename, initialStarCursorFind, commitsBranch, commitCursor);
  			}
  		}
		
	}
	
	
	public void deleteCommits(String branchId) {
		this.commitService.deleteCommits(branchId);
	}


	public void getFirstCommitByBranch(String reponame) throws IOException {
		  
	      CloseableHttpClient httpclient = HttpClients.createDefault();
	      HttpGet httpget = new HttpGet("http://localhost:5050/branchesorder?reponame="+reponame);
	      LOG.info("Request Type: "+httpget.getMethod());
	      
	      HttpResponse httpresponse = httpclient.execute(httpget);

	      HttpEntity entity = httpresponse.getEntity();
	      String jsonData = EntityUtils.toString(entity, "UTF-8");
	      JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
	      
	      Iterator<JsonNode> iter;
	      iter=jsonNode.iterator();
	      
	      JsonNode parameterNode;
	      parameterNode = iter.next();
	      
	      String branchname;
	      String commitoid;
	      
	      List<Commit> firstCommitByBranch = new ArrayList<Commit>();
	      
	      List <String> commitOidRequest = new ArrayList<String>();
	      List <String> branchesNamesRequest = new ArrayList<String>();

	      while(iter.hasNext()){
	        	branchname=parameterNode.get("branchname").textValue();
	        	commitoid=parameterNode.get("commit").textValue();
	        	if(!commitoid.equals("empty")){
	        		commitOidRequest.add(commitoid);
		        	branchesNamesRequest.add(branchname);
	        	}
	        	
	    		parameterNode = iter.next();
	    		if (!iter.hasNext()) {
	    			branchname=parameterNode.get("branchname").textValue();
	    			commitoid=parameterNode.get("commit").textValue();

	    			if(!commitoid.equals("empty")){
		        		commitOidRequest.add(commitoid);
			        	branchesNamesRequest.add(branchname);
		        	}
		        
	    		}
	      }
	      
	      List <Branch> branchesRequest = getBranches(branchesNamesRequest, reponame);
	      
	     
	      boolean seguir=true;
	      int index = 0;
	      for(int i=0; i<branchesRequest.size()&&seguir; i++) {
	    	  if(branchesRequest.get(i).getName().equals("master")) {
	    		  index=i;
	    		  seguir=false;
	    	  }
	      }
	      branchesRequest.remove(index);
	      commitOidRequest.remove(index);
	      
	      for(int i = 0; i<commitOidRequest.size(); i++) {	 
	    	  firstCommitByBranch.add(this.commitService.getCommitByOidyBranch(commitOidRequest.get(i), branchesRequest.get(i).getIdGithub()));
		  }
	   
	      
	      Collections.sort(firstCommitByBranch);  
	      
	      Branch branch;
	      for(int i=0; i<firstCommitByBranch.size(); i++) {
	    	branch = this.branchService.findById(firstCommitByBranch.get(i).getBranchId());
	    	LOG.info(firstCommitByBranch.get(i).getOid() +" --- "+firstCommitByBranch.get(i).getBranchId() + " --- "+ branch.getName());    	
	    	branch.setOrder(i+1);
	     	this.branchService.saveBranch(branch);
	      }
	      
	      branch=this.branchService.getBranchByRepositoryyName(reponame, "master");
     	  branch.setOrder(0);
     	  this.branchService.saveBranch(branch);
	      
     	  httpclient.close();
	}

	private List<Branch> getBranches(List<String> branchesNamesRequest, String reponame) {
		List <Branch> branchesRequest = new ArrayList<Branch>();
		for(int i = 0; i<branchesNamesRequest.size(); i++) {
	    	 branchesRequest.add(this.branchService.getBranchByRepositoryyName(reponame, branchesNamesRequest.get(i)));
	    }
		return branchesRequest;
	}

	

		
}
