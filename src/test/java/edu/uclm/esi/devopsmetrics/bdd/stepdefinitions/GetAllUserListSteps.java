package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
public class GetAllUserListSteps {
	
	private SecureUser secureUser;
	private String jsonData;
	private String jsonDataCopy;

	@Given("user is logging in the system to get all users in the system")
	public void user_is_logging_in_the_system_to_get_all_users_in_the_system() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he gets all the users in the system")
	public void user_is_correct_he_gets_all_the_users_in_the_system() throws ClientProtocolException, IOException {
		
		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		this.secureUser = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 Instant.ofEpochSecond(node.get("tokenValidity").get("epochSecond").longValue())
										);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios/all?tokenpass="+this.secureUser.getTokenPass());

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}
	
	@Then("the users were got correctly")
	public void the_users_were_got_correctly() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios/all?tokenpass="+this.secureUser.getTokenPass());

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonDataCopy = EntityUtils.toString(entity, "UTF-8");
		
		assertEquals(this.jsonData, this.jsonDataCopy);
		
	}


}
