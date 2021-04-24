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
import java.io.UnsupportedEncodingException;
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class UpdateUserSteps {
	
	private SecureUser secureUser;
	private String jsonData;
	
	@Given("user is logging in the system for updating an user")
	public void user_is_logging_in_the_system_for_updating_an_user() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
	}

	@When("user is correct it updates an user by the username and the password in the system")
	public void user_is_correct_it_updates_an_user_by_the_username_and_the_password_in_the_system() throws ClientProtocolException, IOException{

		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		this.secureUser = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 Instant.ofEpochSecond(node.get("tokenValidity").get("epochSecond").longValue())
										);
		
		assertEquals("5f7b28ae85c04e348011de43", this.secureUser.getId());
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut("https://devopsmetrics.herokuapp.com/usuarios/test?tokenpass="+this.secureUser.getTokenPass());
		
		JSONObject json = new JSONObject();
		json.put("password", "test2");
		json.put("role", "dev");
		
		StringEntity params = new StringEntity(json.toString());
		httpput.addHeader("content-type", "application/json");
		
		httpput.setEntity(params);
	    httpclient.execute(httpput);
	}
	
	@Then("by the username and the password in peer not exists and the user is updated correctly")
	public void by_the_username_and_the_password_in_peer_not_exists_and_the_user_is_updated_correctly() throws ParseException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username=test&password=test2");

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
		JsonNode node = new ObjectMapper().readTree(this.jsonData);
		
		this.secureUser = new SecureUser(node.get("id").textValue(),
										 node.get("username").textValue(),
										 node.get("role").textValue(),
										 node.get("tokenPass").textValue(),
										 Instant.ofEpochSecond(node.get("tokenValidity").get("epochSecond").longValue())
										);
		
		assertEquals("test", this.secureUser.getUsername());
		assertEquals("dev", this.secureUser.getRole());
		
	}

}
