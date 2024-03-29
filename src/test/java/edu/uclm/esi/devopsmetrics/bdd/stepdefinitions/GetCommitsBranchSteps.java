package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.devopsmetrics.entities.SecureUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class GetCommitsBranchSteps {
	
	private SecureUser secureUser;
	private String jsonData;
	
	@Given("user is logging in the system for getting all commits from a branch of a repository")
	public void user_is_logging_in_the_system_for_getting_all_commits_from_a_branch_of_a_repository() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he requests all commits from a branch of a repository")
	public void user_is_correct_he_requests_all_commits_from_a_branch_of_a_repository() throws JsonMappingException, JsonProcessingException {
		
		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		String userGithub;
		if (node.get("userGithub") == null) {
			userGithub =  "";
		} else {
			userGithub = node.get("userGithub").textValue();
		}
		
		this.secureUser = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 userGithub
										);
	}
	@Then("the user gets all commits from a branch of a repository")
	public void the_user_gets_all_commits_from_a_branch_of_a_repository() throws ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/commits/commitsbranch?tokenpass="+this.secureUser.getTokenPass()+"&owner=FcoCrespo&reponame=test&branch=main");

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
		JsonNode nodes = new ObjectMapper().readTree(this.jsonData);
		Iterator<JsonNode> iter = nodes.iterator();
		JsonNode parameterNode;

		parameterNode = iter.next();
		String messageHeadline = "";

		if (iter.hasNext()) {
			while (iter.hasNext()) {

				if (parameterNode.get("messageHeadline").textValue().equals("This is a test repository, commit after 2")) {
					messageHeadline = parameterNode.get("messageHeadline").textValue();
				}

				parameterNode = iter.next();
			}
			if (!iter.hasNext()) {
				if (parameterNode.get("messageHeadline").textValue().equals("This is a test repository, commit after 2")) {
					messageHeadline = parameterNode.get("messageHeadline").textValue();
				}
			}
		} else {
			if (parameterNode.get("messageHeadline").textValue().equals("This is a test repository, commit after 2")) {
				messageHeadline = parameterNode.get("messageHeadline").textValue();
			}
		}
		
		assertEquals("This is a test repository, commit after 2", messageHeadline);
	}


}
