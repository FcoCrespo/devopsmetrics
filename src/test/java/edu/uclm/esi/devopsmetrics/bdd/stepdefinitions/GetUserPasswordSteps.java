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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class GetUserPasswordSteps {
	
	private SecureUser secureUser;
	private String jsonData;

	@Given("user is logging in the system")
	public void user_is_logging_in_the_system() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}


	@When("user is correct he gets an user by the username and the password in the system")
	public void user_is_correct_he_gets_an_user_by_the_username_and_the_password_in_the_system() throws JsonMappingException, JsonProcessingException {
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
	@Then("by the username and the password in peer exists and the user is correct")
	public void by_the_username_and_the_password_in_peer_exists_and_the_user_is_correct() {
		
		assertEquals("5f7b28ae85c04e348011de43", this.secureUser.getId());
		assertEquals(System.getProperty("app.user"), this.secureUser.getUsername());	
		assertEquals(System.getProperty("app.password"), this.secureUser.getRole());
	
	}
}
