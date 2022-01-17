package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecureUser.class})
public class DeleteUserSteps {
	
	private SecureUser secureUser;
	private SecureUser secureUserDelete;
	private String jsonData;

	@Given("user is logging in the system for deleting another user")
	public void user_is_logging_in_the_system_for_deleting_another_user() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/usuarios?username="+System.getProperty("app.user")+"&password="+System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he chooses the userId of the user to delete in the system")
	public void user_is_correct_he_chooses_the_user_id_of_the_user_to_delete_in_the_system() throws ClientProtocolException, IOException {
		
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
		HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/usuarios/getuser?username=test&tokenpass="+this.secureUser.getTokenPass());

		HttpResponse httpresponse = httpclient.execute(httpget);
		
		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");

		JsonNode node2 = new ObjectMapper().readTree(this.jsonData);
	
		if (node2.get("userGithub") == null) {
			userGithub =  "";
		} else {
			userGithub = node2.get("userGithub").textValue();
		}
				
		this.secureUserDelete = new SecureUser(node2.get("id").textValue(),
										 node2.get("username").textValue(),
										 node2.get("role").textValue(),
										 "",
										 userGithub
										);
		
		
	}
	@Then("the choosed user was deleted of the system")
	public void the_choosed_user_was_deleted_of_the_system() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDelete httpdelete = new HttpDelete(System.getProperty("server.url")+"/usuarios/deleteuser?username="+this.secureUserDelete.getUsername()+"&tokenpass="+this.secureUser.getTokenPass());
		
		httpclient.execute(httpdelete);
		
	}
}
