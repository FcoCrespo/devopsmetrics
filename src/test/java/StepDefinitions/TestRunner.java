package StepDefinitions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@CucumberContextConfiguration 
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue="StepDefinitions",
monochrome =true,
plugin = {"json:target/cucumber/TestReport.json","pretty", "junit:target/cucumber/TestReport.xml","html:target/cucumber/TestReport.html"}
		)
public class TestRunner {

    @AfterClass
    public static void setupAfter() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");  
 	   	LocalDateTime now = LocalDateTime.now();  
    	File fJSONoriginal = new File("target/cucumber/TestReport.json");
    	File fJSONrenombrado = new File("target/cucumber/TestReport"+dtf.format(now)+".json");
    	File fJUnitoriginal = new File("target/cucumber/TestReport.xml");
    	File fJUnitrenombrado = new File("target/cucumber/TestReport"+dtf.format(now)+".xml");
    	File fHTMLoriginal = new File("target/cucumber/TestReport.html");
    	File fHTMLrenombrado = new File("target/cucumber/TestReport"+dtf.format(now)+".html");
    	fJSONoriginal.renameTo(fJSONrenombrado);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);
    }
    
}