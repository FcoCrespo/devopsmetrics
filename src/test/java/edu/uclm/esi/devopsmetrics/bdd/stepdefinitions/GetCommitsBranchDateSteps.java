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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class GetCommitsBranchDateSteps {
	
	private SecureUser secureUser;
	private String jsonData;

	@Given("user is logging in the system for getting all commits from a branch of a repository between a date")
	public void user_is_logging_in_the_system_for_getting_all_commits_from_a_branch_of_a_repository_between_a_date() throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he requests all commits from a branch of a repository between a date")
	public void user_is_correct_he_requests_all_commits_from_a_branch_of_a_repository_between_a_date() throws JsonMappingException, JsonProcessingException {

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
	@Then("the user gets all commits from a branch of a repositorybetween a date")
	public void the_user_gets_all_commits_from_a_branch_of_a_repositorybetween_a_date() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://devopsmetrics.herokuapp.com/commits/commitsbranchdate?tokenpass="+this.secureUser.getTokenPass());
		
		JSONObject json = new JSONObject();
		json.put("reponame", "test");
		json.put("branch", "main");
		json.put("owner", "FcoCrespo");
		json.put("begindate", "25/04/2021 10:00");
		json.put("enddate", "25/04/2021 22:00");
		
		StringEntity params = new StringEntity(json.toString());
		httppost.addHeader("content-type", "application/json");
		
		httppost.setEntity(params);
	    httpclient.execute(httppost);

	    HttpResponse httpresponse = httpclient.execute(httppost);
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
