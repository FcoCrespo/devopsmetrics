package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.devopsmetrics.models.SecureUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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
public class RegistrarUsuarioSteps {
	
	private SecureUser secureUser;
	private String jsonData;

	@Given("user is logging in the system for register other")
	public void user_is_logging_in_the_system_for_register_other() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he registers an user by the username, the password and the role in the system")
	public void user_is_correct_he_registers_an_user_by_the_username_the_password_and_the_role_in_the_system() throws ClientProtocolException, IOException {
		
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
		
		assertEquals("5f7b28ae85c04e348011de43", this.secureUser.getId());
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://devopsmetrics.herokuapp.com/usuarios?tokenpass="+this.secureUser.getTokenPass());
		
		JSONObject json = new JSONObject();
		json.put("username", "test");
		json.put("role", "admin");
		json.put("email", "esidevopsmetrics@gmail.com");
		json.put("userGithub", "");
		
		StringEntity params = new StringEntity(json.toString());
		httppost.addHeader("content-type", "application/json");
		
		httppost.setEntity(params);
	    httpclient.execute(httppost);
		
	}
	@Then("by the username and the password in peer not exists and the user is registered correctly")
	public void by_the_username_and_the_password_in_peer_not_exists_and_the_user_is_registered_correctly() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios/getuser?username=test&tokenpass="+this.secureUser.getTokenPass());

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
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
										 "",
										 userGithub
										);
		
		assertEquals("test", this.secureUser.getUsername());
		assertEquals("admin", this.secureUser.getRole());
		
	}
}
