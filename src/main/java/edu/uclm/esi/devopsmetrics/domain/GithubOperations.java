package edu.uclm.esi.devopsmetrics.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;
import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitService;

@Service
@Scope("singleton")
@Configuration
@EnableWebSecurity(debug = false) 
public class GithubOperations {
	
	  

	  private final CommitService commitService;
	  private final BranchService branchService;
	  private final BranchesGithub branchesGithub;
	  private final CommitsGithub commitsGithub;
	  
	  private final Logger logger;
	  
	  /**
		 * @author FcoCrespo
		 */
		public GithubOperations (final CommitService commitService, final BranchService branchService, 
				final BranchesGithub branchesGithub, final CommitsGithub commitsGithub) {
			
			this.commitService = commitService;
			this.branchService =  branchService;
			this.branchesGithub = branchesGithub;
			this.commitsGithub = commitsGithub;
			this.logger = Logger.getLogger(GithubOperations.class.getName());
			
		}

	public String getBranches(String reponame, String owner) {
		
		try {
			this.branchesGithub.getBranches(reponame, owner);
			List <Branch> listBranches = this.branchService.getBranchesByRepository(reponame, false);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			return ow.writeValueAsString(listBranches);
		} catch (JsonProcessingException e) {
			logger.log(Level.INFO, e.toString());
			return null;
		}
		
	}

	public void getCommits(String reponame, String owner) throws IOException {
		
		getBranches(reponame, owner);
		
		List<Branch>branches = this.branchService.getBranchesByRepository(reponame, false);
		
		Commit commitDeRepo;
		
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
  			logger.log(Level.INFO,"INTRODUCIENDO NUEVOS COMMITS");
  			filename = "src/main/resources/graphql/commits.graphql";
  			for(int i = 0; i< branches.size(); i++) {
  				info[2]=branches.get(i).getName();
  				info[3]=branches.get(i).getIdGithub();
  	  			this.commitsGithub.getNewRepositoryCommits(info, filename);
  			}
  		}
  		else {
  			logger.log(Level.INFO,"ACTUALIZANDO COMMITS");
  			filename = "src/main/resources/graphql/commits-cursor-before.graphql";		
  			List<Commit> commitsBranch = new ArrayList<Commit>();
  			CommitCursor commitCursor= null;
  			
  			for(int i = 0; i< branches.size(); i++) {
  				info[2]=branches.get(i).getName();
  				info[3]=branches.get(i).getIdGithub();
  	  			this.commitsGithub.updateRepositoryCommits(info, filename, initialStarCursorFind, commitsBranch, commitCursor);
  			}
  		}
		
	}

		
}