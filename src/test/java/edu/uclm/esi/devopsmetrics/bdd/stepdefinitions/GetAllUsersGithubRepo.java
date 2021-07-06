package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.devopsmetrics.models.SecureUser;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class GetAllUsersGithubRepo {
	private SecureUser secureUser;
	private String jsonData;
	@Given("user is logging in the system for getting users of github in a repository")
	public void user_is_logging_in_the_system_for_getting_users_of_github_in_a_repository() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
	}


	@When("user is correct he gets an user by the username and the password in the system for getting users of github in a repository")
	public void user_is_correct_he_gets_an_user_by_the_username_and_the_password_in_the_system_for_getting_users_of_github_in_a_repository() throws JsonMappingException, JsonProcessingException {
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
	@Then("by the username and the password in peer exists and the user get users of github in a repository")
	public void by_the_username_and_the_password_in_peer_exists_and_the_user_get_users_of_github_in_a_repository() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/commits/usersgithubrepo?tokenpass="+this.secureUser.getTokenPass()+"&owner=sherrerap&reponame=eSalud");

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		JsonNode nodes = new ObjectMapper().readTree(this.jsonData);
		Iterator<JsonNode> iter = nodes.iterator();
		JsonNode parameterNode;

		parameterNode = iter.next();
		String login = "";

		if (iter.hasNext()) {
			while (iter.hasNext()) {

				if (parameterNode.get("login").textValue().equals("FcoCrespo")) {
					login = parameterNode.get("login").textValue();
				}

				parameterNode = iter.next();
			}
			if (!iter.hasNext()) {
				if (parameterNode.get("login").textValue().equals("FcoCrespo")) {
					login = parameterNode.get("login").textValue();
				}
			}
		} else {
			if (parameterNode.get("login").textValue().equals("FcoCrespo")) {
				login = parameterNode.get("login").textValue();
			}
		}
		
		assertEquals("FcoCrespo", login);
	}

}
