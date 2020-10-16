package edu.uclm.esi.devopsmetrics.utilities;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

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
import edu.uclm.esi.devopsmetrics.services.CommitCursorService;
import edu.uclm.esi.devopsmetrics.services.CommitService;
import edu.uclm.esi.devopsmetrics.models.Branch;
import edu.uclm.esi.devopsmetrics.models.Commit;
import edu.uclm.esi.devopsmetrics.models.CommitCursor;


@Service
@Scope("singleton")
public class CommitsGithub{
	
	@Autowired
	private Environment env;
	
	private final BranchService branchService;
	private final CommitService commitService;
	private final CommitCursorService commitCursorService;
	
	private final OkHttpClient client = new OkHttpClient();
	private final String graphqlUri = "https://api.github.com/graphql";


	  /**
	   * @author FcoCrespo
	   */
	public CommitsGithub(final BranchService branchService,
						 final CommitService commitService,
						 final CommitCursorService commitCursorService) {
	    this.branchService = branchService;
	    this.commitService = commitService;
	    this.commitCursorService = commitCursorService;

	}
	
	public void getCommits(String reponame, String owner) throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException{
		
		//hace falta un comprobacion previa de nuevos commits en el repositorio
		
		String graphqlPayload;
		
		String filename = "src/main/resources/graphql/commits.graphql";
	    
	    File file = new File(filename);
	    
	    // Create a variables to pass to the graphql query
        ObjectNode variables;
        Commit commit;
        CommitCursor commitCursor=null;
        
        String idGithub, oid;
  		String messageHeadline, message, messageBody;
  		String pushedDate, changedFiles;
  		String authoredByCommitter, authoredDate;
  		String authorName, authorEmail, authorDate, authorId;
 
  		String jsonData;
  		JsonNode jsonNode, nodes, cursorNode, parameterNode, nodeAuthor, nodeAuthorId;
  		Response response;
  		Iterator<JsonNode> iter;
  		
  		//Actualizamos las ramas
  		getBranches(reponame, owner);
        
        List<Branch> branches = branchService.getBranchesByRepository(reponame);
        

		for(int i= 0; i<branches.size(); ) {
			if(filename.equals("src/main/resources/graphql/commits.graphql")) {
				variables= new ObjectMapper().createObjectNode();
		        variables.put("owner", owner);
		        variables.put("repo", reponame);
		        variables.put("branch", branches.get(i).getName());

			}
			else {
				variables= new ObjectMapper().createObjectNode();
		        variables.put("owner", owner);
		        variables.put("repo", reponame);
		        System.out.println(commitCursor.getEndCursor());
		        variables.put("branch", branches.get(i).getName());
			}
			System.out.println(file.getPath());
	    
	        // Now parse the graphql file to a request payload string
	        graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
	        
	        response = prepareResponse(graphqlPayload, client, graphqlUri);
			
	        jsonData = response.body().string();
	        jsonNode = new ObjectMapper().readTree(jsonData);
	        
	        commitCursor = updateCommitCursor(jsonNode, branches.get(i).getName(), reponame);
	        
	    
	        nodes = jsonNode.path("data").path("repository").path("ref").path("target").path("history").path("nodes");
	        iter = nodes.iterator();
	        parameterNode = iter.next();
	        
	        while(iter.hasNext()){
	        	
	        	idGithub = comprobarValor(parameterNode, "id");
	      		oid = comprobarValor(parameterNode,"oid");
	      	    messageHeadline = comprobarValor(parameterNode,"messageHeadline");
	      		message = comprobarValor(parameterNode,"message");
	      		messageBody = comprobarValor(parameterNode,"messageBody");
	      		pushedDate = comprobarValor(parameterNode,"pushedDate");
	      		changedFiles = comprobarValor(parameterNode,"changedFiles");
	      		authoredByCommitter = comprobarValor(parameterNode,"authoredByCommitter");
	      		authoredDate = comprobarValor(parameterNode,"authoredDate");
	      		
	      		nodeAuthor = parameterNode.path("author");
	      		if(nodeAuthor==null) {
	      			authorId = "";
	      			authorName = "";
		      		authorEmail = "";
		      		authorDate = "";
	      		}
	      		else {
	      			authorName = comprobarValor(nodeAuthor,"name");
		      		authorEmail = comprobarValor(nodeAuthor,"email");
		      		authorDate =comprobarValor(nodeAuthor,"date");
		      		
		      		nodeAuthorId = nodeAuthor.path("user");
		      		if(nodeAuthorId==null) {
		      			authorId = ""; 
		      		}
		      		else {
		      			authorId = comprobarValor(nodeAuthorId,"id");
		      		}
	      		}
	  	
	      		commit = new Commit(idGithub, oid, messageHeadline, message, messageBody, pushedDate, changedFiles, authoredByCommitter,
	      							authoredDate, authorName, authorEmail, authorDate, authorId, branches.get(i).getName(), reponame);
	      		
	      		commitService.saveCommit(commit);
	      		
	      		parameterNode = iter.next();
	    	}
	        
	        if(commitCursor.getHasNextPage()==true) {
	        	filename = "src/main/resources/graphql/commits-cursor.graphql";
	        	file = new File(filename);
	        }
	        else {
	        	filename = "src/main/resources/graphql/commits.graphql";
	        	file = new File(filename);
	        	i++;
	        }
		}

	}

	private String comprobarValor(JsonNode parameterNode, String textValue) {
		
		if(parameterNode.get(textValue)==null) {
			return "";
		}
		else {
			return parameterNode.get(textValue).textValue();
		}
		
	}

	private CommitCursor updateCommitCursor(JsonNode jsonNode, String branch, String reponame) {
		
		JsonNode cursorNode = jsonNode.path("data").path("repository").path("ref").path("target").path("history").path("pageInfo");
        
        boolean hasNextPage = cursorNode.get("hasNextPage").booleanValue();
        String endCursor = cursorNode.get("endCursor").textValue();
        String startCursor = cursorNode.get("startCursor").textValue();
        
        CommitCursor commitCursor = commitCursorService.getCommitCursorByEndCursoryHasNextPage(branch, reponame);
        
        if(commitCursor==null) {
        	commitCursor =  new CommitCursor(hasNextPage, endCursor, startCursor, branch, reponame);
        	commitCursorService.saveCommitCursor(commitCursor);
        }
        else {
        	commitCursor.setHasNextPage(hasNextPage);
        	commitCursor.setEndCursor(endCursor);
        	
        	commitCursorService.updateCommitCursor(commitCursor);
        }
        
        System.out.println(commitCursor.toString());
        
        return commitCursor;
		
	}

	public void getBranches(String reponame, String owner) throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException{
		
	    String graphqlPayload;
	    
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
   
        introducirRama(parameterNode, reponame);
        
        while(iter.hasNext()){
        	introducirRama(parameterNode, reponame);
    		parameterNode = iter.next();
    		if (!iter.hasNext()) {
    			introducirRama(parameterNode, reponame);
    		}
    	}
       
	}
	
	
	
	private void introducirRama(JsonNode parameterNode, String reponame) {
		String idGithub;
		Branch branch;
        Branch branchBD;
        
        String branchName = parameterNode.get("branchName").textValue();
        branchBD = branchService.getBranchByRepositoryyName(reponame, branchName);	
		if(branchBD==null) {
			
    		idGithub = parameterNode.get("id").textValue();
    		branch = new Branch(idGithub, reponame, branchName);
			branchService.saveBranch(branch);
		}
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