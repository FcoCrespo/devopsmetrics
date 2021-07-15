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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SecureUser.class })
public class GetAllTestMetricsSteps {

	private SecureUser secureUser;
	private String jsonData;
	
	@Given("user is logging in the system for getting all test metrics from a repository by his owner")
	public void user_is_logging_in_the_system_for_getting_all_test_metrics_from_a_repository_by_his_owner() throws ClientProtocolException, IOException {
	    
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/usuarios?username="
				+ System.getProperty("app.user") + "&password=" + System.getProperty("app.password"));

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		this.jsonData = EntityUtils.toString(entity, "UTF-8");
		
	}

	@When("user is correct he requests all test metrics from a repository by his owner")
	public void user_is_correct_he_requests_all_test_metrics_from_a_repository_by_his_owner() throws JsonMappingException, JsonProcessingException {
		
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
	
	@Then("the user gets all test metrics from a repository by his owner")
	public void the_user_gets_all_test_metrics_from_a_repository_by_his_owner() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://devopsmetrics.herokuapp.com/metrics/alltestmetrics?tokenpass="
				+ this.secureUser.getTokenPass() + "&reponame=devopsmetrics&owner=FcoCrespo");

		HttpResponse httpresponse = httpclient.execute(httpget);

		HttpEntity entity = httpresponse.getEntity();
		String respuesta = EntityUtils.toString(entity, "UTF-8");

		JsonNode jsonNode = new ObjectMapper().readTree(respuesta);

		Iterator<JsonNode> iter;
		iter = jsonNode.iterator();

		JsonNode parameterNode;
		parameterNode = iter.next();
		
		boolean encontrado=false;

		while (iter.hasNext()&&!encontrado) {

			if(parameterNode.get("idMethodTest").textValue().equals("608fd03b96f4674354296ea3")){
				encontrado=true;
			}
			parameterNode = iter.next();
			if (!iter.hasNext()&&!encontrado) {

				if(parameterNode.get("idMethodTest").textValue().equals("608fd03b96f4674354296ea3")){
					encontrado=true;
				}

			}
		}
		
		assertEquals(true, encontrado);
	    
	}


}
