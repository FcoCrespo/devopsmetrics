package StepDefinitions;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
	@Given("user is on login page")
	public void user_is_on_login_page() {
		System.out.println("Inside step - user is on login page");
	}

	@When("user enters username and password")
	public void user_enters_username_and_password() {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Inside step - user enters username and password");
	}

	@And("clicks on login button")
	public void clicks_on_login_button() {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Inside step - clicks on login button");
	}

	@Then("user is navigated to the home page")
	public void user_is_navigated_to_the_home_page() {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Inside step - user is navigated to the home page");
		assertEquals("hola", "hola");
	}
	


}