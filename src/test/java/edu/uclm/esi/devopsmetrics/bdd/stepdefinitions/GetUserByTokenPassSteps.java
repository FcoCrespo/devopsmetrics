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
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class GetUserByTokenPassSteps {

	private SecureUser secureUser;
	private String jsonData;
	private SecureUser secureUserTokenPass;
	
	@Given("user is logging in the system for get his tokenpass")
	public void user_is_logging_in_the_system_for_get_his_tokenpass() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
	}

	@When("user is correct he gets an user by the tokenpass of the session in the system")
	public void user_is_correct_he_gets_an_user_by_the_tokenpass_of_the_session_in_the_system() throws ClientProtocolException, IOException {
		
		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		this.secureUser = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 Instant.ofEpochSecond(node.get("tokenValidity").get("epochSecond").longValue())
										);
	
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios/"+System.getProperty("app.user")+"?tokenpass="+this.secureUser.getTokenPass());

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
	
	}
	@Then("the tokenpass exists and the user is correct")
	public void the_tokenpass_exists_and_the_user_is_correct() throws JsonMappingException, JsonProcessingException {

		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		this.secureUserTokenPass = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 Instant.ofEpochSecond(node.get("tokenValidity").get("epochSecond").longValue())
										);
		
		assertEquals(this.secureUserTokenPass.getTokenPass(), this.secureUser.getTokenPass());
	}

}
