package edu.uclm.esi.devopsmetrics.domain;


import java.io.File;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;
import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;


@Service
@Scope("singleton")
public class CommitOperations{
	
	@Autowired
	private Environment env;
	
	private final BranchService branchService;
	private final CommitService commitService;
	private final CommitCursorService commitCursorService;
	
	
	private static String graphqlUri = "https://api.github.com/graphql";


	  /**
	   * @author FcoCrespo
	   */
	public CommitOperations(final BranchService branchService,
						 final CommitService commitService,
						 final CommitCursorService commitCursorService) {
	    this.branchService = branchService;
	    this.commitService = commitService;
	    this.commitCursorService = commitCursorService;

	}
	
	

	public void getBranches(String reponame, String owner){
		
	    String graphqlPayload;
	    
	    File file = new File("src/main/resources/graphql/branches.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("owner", owner);
        variables.put("repo", reponame);

        // Now parse the graphql file to a request payload string
        try {
			graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
			 // Build and trigger the request
	        Response response = prepareResponse(graphqlPayload, graphqlUri);
			
	        if(response!=null) {
	        	String jsonData = response.body().string();
		        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
		        
		        JsonNode nodes = jsonNode.path("data").path("repository").path("refs").path("nodes");
		        Iterator<JsonNode> iter = nodes.iterator();
		        JsonNode parameterNode = iter.next();
		   
		        introducirRama(parameterNode, reponame);
		        
		        while(iter.hasNext()){
		        	introducirRama(parameterNode, reponame);
		    		parameterNode = iter.next();
		    		if (!iter.hasNext()) {
		    			introducirRama(parameterNode, reponame);
		    		}
		    	}
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
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
	

	private Response prepareResponse(String graphqlPayload, String graphqlUri) {
		OkHttpClient clientHTTP = new OkHttpClient().newBuilder()
	            .connectTimeout(5, TimeUnit.MINUTES) 
	            .writeTimeout(5, TimeUnit.MINUTES) 
	            .readTimeout(5, TimeUnit.MINUTES) 
	            .build();
		
	    @SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = new Request.Builder()
        		.url(graphqlUri)
        		.addHeader("Authorization", "Bearer " + env.getProperty("github.token") )
        		.post(body)
        		.build();
        Response response;
		try {
			response = clientHTTP.newCall(request).execute();
			 if(response.isSuccessful()){
		            return response;             
		     }else return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
       
	
	}

	
}