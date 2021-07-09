package edu.uclm.esi.devopsmetrics.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.devopsmetrics.utilities.Utilities;
import edu.uclm.esi.devopsmetrics.utilities.SleepClass;

import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Utilities.class)
public class LoginSteps {

	WebDriver driver = null;
	
	@Given("user is on login page")
	public void user_is_on_login_page() throws InterruptedException {
		System.out.println("Inside step - user is on login page");
		
		Path path = FileSystems.getDefault().getPath("src/test/resources/drivers/chromedriver.exe");
		
		System.setProperty("webdriver.chrome.driver", path.toString());
		
		driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
	
		driver.get("https://webesidevopsmetrics.herokuapp.com/repos");

	}

	@When("user enters username and password")
	public void user_enters_username_and_password() {
		
		driver.findElement(By.xpath("//input[@placeholder='user']")).sendKeys(System.getProperty("app.user"));
	    driver.findElement(By.xpath("//input[@placeholder='password']")).sendKeys(System.getProperty("app.password"));    
	}

	@And("clicks on login button")
	public void clicks_on_login_button() {
		
		driver.findElement(By.xpath("//button[@value='Login']")).click();
		
	}

	@Then("user is navigated to the home page")
	public void user_is_navigated_to_the_home_page() throws InterruptedException {	
		SleepClass.sleep(1000);
		String expectedUrl = driver.getCurrentUrl();
		assertEquals("https://webesidevopsmetrics.herokuapp.com/repos", expectedUrl);	
		
		driver.quit();
	}
	


}