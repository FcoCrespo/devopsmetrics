package edu.uclm.esi.devopsmetrics.utilities;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.uclm.esi.devopsmetrics.services.UserService;
import edu.uclm.esi.devopsmetrics.utilities.GraphqlTemplate;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import edu.uclm.esi.devopsmetrics.services.BranchService;
import edu.uclm.esi.devopsmetrics.models.Branch;


@Service
@Scope("singleton")
public class CommitsGithub{
	
	@Autowired
	private Environment env;
	
	private final BranchService branchService;


	  /**
	   * @author FcoCrespo
	   */
	public CommitsGithub(final BranchService branchService) {
	    this.branchService = branchService;
	}

	public String getBranches(String reponame, String owner) throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException{
		
		OkHttpClient client = new OkHttpClient();
        String graphqlUri = "https://api.github.com/graphql";
	    String graphqlPayload;
	    
	    
	    Git git;
	    Repository repo;
	    String githubuser;
	    String githubkey;
	    
	    
		graphqlUri = "https://api.github.com/graphql";
	    
	        
	    githubuser = env.getProperty("github.user"); 
	    githubkey = env.getProperty("github.key"); 
		
	    
	    File file = new File("src/main/resources/graphql/branches.graphql");

        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("owner", owner);
        variables.put("repo", reponame);

        // Now parse the graphql file to a request payload string
        graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);

        // Build and trigger the request
        Response response = prepareResponse(graphqlPayload, client, graphqlUri);
		
        String jsonData = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonData);
        
        JsonNode nodes = jsonNode.path("data").path("repository").path("refs").path("nodes");
        Iterator<JsonNode> iter = nodes.iterator();
        JsonNode parameterNode = iter.next();
        
        JsonNode nodeRepo = jsonNode.path("data").path("repository");
        
        String repoName = nodeRepo.get("name").textValue();
        Branch branch;
        
        Branch branchBD;
        
        String branchName;
        String idGithub;
        
        while(iter.hasNext()){
    		
    		branchName = parameterNode.get("branchName").textValue();
    		idGithub = parameterNode.get("id").textValue();
    		
    		branch = new Branch(idGithub, repoName, branchName);
    		
    		branchBD = branchService.getBranchByRepositoryyName(repoName, branchName);
    		
    		if(branchBD==null) {
    			branchService.saveBranch(branch);
    		}
    		
    		parameterNode = iter.next();
    		
    	}
        
        
       
        return jsonData;
       
	}
	
	private Response prepareResponse(String graphqlPayload, OkHttpClient client, String graphqlUri) throws IOException {
		 
	    RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = new Request.Builder()
        		.url(graphqlUri)
        		.addHeader("Authorization", "Bearer " + env.getProperty("github.token") )
        		.post(body)
        		.build();
        return client.newCall(request).execute();
	
	}

	
}