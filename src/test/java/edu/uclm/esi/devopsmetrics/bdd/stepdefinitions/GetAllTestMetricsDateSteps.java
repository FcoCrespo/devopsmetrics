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
@SpringBootTest(classes = { SecureUser.class })
public class GetAllTestMetricsDateSteps {
	
	
	private SecureUser secureUser;
	private String jsonData;



		@Given("user is logging in the system for getting all test metrics from a repository by his owner between two dates")
		public void user_is_logging_in_the_system_for_getting_all_test_metrics_from_a_repository_by_his_owner_between_two_dates() throws ClientProtocolException, IOException {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(System.getProperty("server.url")+"/usuarios?username="
					+ System.getProperty("app.user") + "&password=" + System.getProperty("app.password"));

			HttpResponse httpresponse = httpclient.execute(httpget);

			HttpEntity entity = httpresponse.getEntity();
			this.jsonData = EntityUtils.toString(entity, "UTF-8");
		}
		
		@When("user is correct he requests all test metrics from a repository by his owner between two dates")
		public void user_is_correct_he_requests_all_test_metrics_from_a_repository_by_his_owner_between_two_dates() throws JsonMappingException, JsonProcessingException {
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
		@Then("the user gets all test metrics from a repository by his owner between two dates")
		public void the_user_gets_all_test_metrics_from_a_repository_by_his_owner_between_two_dates() throws ClientProtocolException, IOException {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(System.getProperty("server.url")+"/metrics/alltestmetricsdate?tokenpass="
					+ this.secureUser.getTokenPass());

			JSONObject json = new JSONObject();
			json.put("reponame", "devopsmetrics");
			json.put("owner", "FcoCrespo");
			json.put("begindate", "31/05/2021 00:00");
			json.put("enddate", "01/06/2021 00:00");

			StringEntity params = new StringEntity(json.toString());
			httppost.addHeader("content-type", "application/json");

			httppost.setEntity(params);
			httpclient.execute(httppost);

			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			this.jsonData = EntityUtils.toString(entity, "UTF-8");

			JsonNode nodes = new ObjectMapper().readTree(this.jsonData);
			JsonNode parameterNode;
			Iterator<JsonNode> iter = nodes.iterator();

			parameterNode = iter.next();
			String test = "";

			if (iter.hasNext()) {
				while (iter.hasNext()) {

					if (parameterNode.get("idMethodTest").textValue().equals("60b50334e42e5d405438045b") && parameterNode.get("idTest").textValue().equals("60b50332e42e5d4054380451")) {
						test = parameterNode.get("idMethodTest").textValue()+ " " + parameterNode.get("idTest").textValue();
					}

					parameterNode = iter.next();
				}
				if (!iter.hasNext()) {
					if (parameterNode.get("idMethodTest").textValue().equals("60b50334e42e5d405438045b") && parameterNode.get("idTest").textValue().equals("60b50332e42e5d4054380451")) {
						test = parameterNode.get("idMethodTest").textValue()+ " " + parameterNode.get("idTest").textValue();
					}
				}
			} else { 
				if (parameterNode.get("idMethodTest").textValue().equals("60b50334e42e5d405438045b") && parameterNode.get("idTest").textValue().equals("60b50332e42e5d4054380451")) {
					test = parameterNode.get("idMethodTest").textValue()+ " " + parameterNode.get("idTest").textValue();
				}
			}

			assertEquals("60b50334e42e5d405438045b 60b50332e42e5d4054380451", test);
		}


}
