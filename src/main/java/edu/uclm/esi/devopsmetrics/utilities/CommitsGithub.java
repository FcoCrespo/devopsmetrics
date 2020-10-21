package edu.uclm.esi.devopsmetrics.utilities;


import java.io.File;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

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
	
	public void getCommits(String reponame, String owner) throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException, ParseException{
		
		
		    //hace falta un comprobacion previa de nuevos commits en el repositorio
		
			String graphqlPayload;
			
			String filename = "src/main/resources/graphql/commits.graphql";
		    
		    File file = new File(filename);
		    
		    // Create a variables to pass to the graphql query
	        ObjectNode variables;
	        
	        CommitCursor commitCursor=null;
	        
	        
	  		String jsonData;
	  		JsonNode jsonNode, nodes, parameterNode;
	  		Response response;
	  		Iterator<JsonNode> iter;
	  		
	  		//Actualizamos las ramas
	  		getBranches(reponame, owner);
	        
	        List<Branch> branches = branchService.getBranchesByRepository(reponame);
	        

			for(int i= 0; i<branches.size(); ) {
				System.out.println(file.getPath());
				variables= new ObjectMapper().createObjectNode();
		        variables.put("owner", owner);
		        variables.put("repo", reponame);
		        variables.put("branch", branches.get(i).getName());
		        System.out.println("Estamos en la rama: "+branches.get(i).getName());
				if(filename.equals("src/main/resources/graphql/commits-cursor.graphql")) {
					variables.put("cursor", commitCursor.getEndCursor());
				}
		        // Now parse the graphql file to a request payload string
		        graphqlPayload = GraphqlTemplate.parseGraphql(file, variables);
		        
		        response = prepareResponse(graphqlPayload, graphqlUri);
				
		        jsonData = response.body().string();
		        jsonNode = new ObjectMapper().readTree(jsonData);
		        
		        commitCursor = updateCommitCursor(jsonNode, branches.get(i).getName(), reponame);
		        
		        nodes = jsonNode.path("data").path("repository").path("ref").path("target").path("history").path("nodes");
		        iter = nodes.iterator();
		        parameterNode = iter.next();

		        
		        while(iter.hasNext()){
		        	introducirCommit(parameterNode, reponame, branches.get(i).getName());
		    		parameterNode = iter.next();
		    		if (!iter.hasNext()) {
		    			introducirCommit(parameterNode, reponame, branches.get(i).getName());
		    		}
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

	private void introducirCommit(JsonNode parameterNode, String reponame, String branch) throws ParseException {
		Commit commit;
        String idGithub, oid;
  		String messageHeadline, message, messageBody;
  		int changedFiles;
  		String authoredByCommitter, authorName, authorEmail, authorDate, authorId;
  		Instant pushedDate, authoredDate; 
  		
  		String pushedDateExtraido, authoredDateExtraido; 
  		
  		JsonNode nodeAuthor, nodeAuthorId;
 
  		idGithub = comprobarValor(parameterNode, "id");
  		oid = comprobarValor(parameterNode,"oid");
  	    messageHeadline = comprobarValor(parameterNode,"messageHeadline");
  		message = comprobarValor(parameterNode,"message");
  		messageBody = comprobarValor(parameterNode,"messageBody");
  		pushedDateExtraido = comprobarValor(parameterNode,"pushedDate");
  		changedFiles = comprobarValorchangedFiles(parameterNode,"changedFiles");
  		authoredByCommitter = comprobarValor(parameterNode,"authoredByCommitter");
  		authoredDateExtraido = comprobarValor(parameterNode,"authoredDate");
  		
  		if(pushedDateExtraido==null || pushedDateExtraido.equals("")) {
  			pushedDate = null;
  		}
  		else {
  			pushedDate = Instant.parse(pushedDateExtraido);
  		}
  		
  		if(authoredDateExtraido==null || authoredDateExtraido.equals("")) {
  			authoredDate = null;
  		}
  		else {
  			authoredDate = Instant.parse(authoredDateExtraido);
  		}
  		
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
  		
  		System.out.println(idGithub +" : idGithub y oid : "+ oid);
  		System.out.println("MessageHeadline: "+messageHeadline);
	
  		commit = new Commit(idGithub, oid, messageHeadline, message, messageBody, pushedDate, changedFiles, authoredByCommitter,
  							authoredDate, authorName, authorEmail, authorDate, authorId, branch, reponame);
  		
  		commitService.saveCommit(commit);
	}

	private int comprobarValorchangedFiles(JsonNode parameterNode, String value) {		
		if(parameterNode.get(value)==null) {
			return 0;
		}
		else {
			return parameterNode.get(value).intValue();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    		branch = new Branch(idGithub, reponame, branchName, 0);
			branchService.saveBranch(branch);
		}
	}

	private Response prepareResponse(String graphqlPayload, String graphqlUri) {
		OkHttpClient client = new OkHttpClient().newBuilder()
	            .connectTimeout(5, TimeUnit.MINUTES) 
	            .writeTimeout(5, TimeUnit.MINUTES) 
	            .readTimeout(5, TimeUnit.MINUTES) 
	            .build();
		
	    RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), graphqlPayload);
        Request request = new Request.Builder()
        		.url(graphqlUri)
        		.addHeader("Authorization", "Bearer " + env.getProperty("github.token") )
        		.post(body)
        		.build();
        Response response;
		try {
			response = client.newCall(request).execute();
			 if(response.isSuccessful()){
		            return response;             
		     }else return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
       
	
	}

	
}
