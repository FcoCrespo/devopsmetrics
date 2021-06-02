package edu.uclm.esi.devopsmetrics.domain;


import java.io.File;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.Response;

import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;
import edu.uclm.esi.devopsmetrics.models.Branch;


@Service
public class BranchesGithub{
	
	private final BranchService branchService;
	private final ResponseHTTP response;
	
	private String graphqlUri;


	/**
	   * @author FcoCrespo
	   */
	public BranchesGithub(final BranchService branchService, final ResponseHTTP response) {
	   
		this.branchService = branchService;
		this.response = response;
		this.graphqlUri = "https://api.github.com/graphql";
		
	}
	
	public void getBranches(String reponame, String owner){
		
	    String graphqlPayload;
	    
	    File file = new File("src/main/resources/graphql/branches.graphql");

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("owner", owner);
        variables.put("repo", reponame);

        try {
  
			graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
	        Response responseGiven = response.prepareResponse(graphqlPayload, this.graphqlUri, owner);
			
	        if(responseGiven!=null) {
	        	String jsonData = responseGiven.body().string();
		        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
		        
		        JsonNode nodes = jsonNode.path("data").path("repository").path("refs").path("nodes");
		        Iterator<JsonNode> iter = nodes.iterator();
		        JsonNode parameterNode = iter.next();
		   
		        introducirRama(parameterNode, reponame);
		        
		        while(iter.hasNext()){
		        	introducirRama(parameterNode, reponame);
		    		parameterNode = iter.next();	    		
		    	}
		        if (!iter.hasNext()) {
	    			introducirRama(parameterNode, reponame);
	    		}
	        }
	        
		} catch (IOException e) {
			e.toString();
		}
	}
	
	
	
	private void introducirRama(JsonNode parameterNode, String reponame) {
		String idGithub;
		Branch branch;
        Branch branchBD;
        
        String branchName = parameterNode.get("branchName").textValue();
        branchBD = branchService.getBranchByRepositoryyName(reponame, branchName);	
		if(branchBD==null && !branchName.contains("dependabot/npm_and_yarn")) {
    		idGithub = parameterNode.get("id").textValue();
    		branch = new Branch(idGithub, reponame, branchName, -1);
			branchService.saveBranch(branch);
		}
	}
	

	
}