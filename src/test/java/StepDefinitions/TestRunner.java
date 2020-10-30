package StepDefinitions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
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

	final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");  
	final static LocalDateTime now = LocalDateTime.now(); 
	@BeforeClass
    public static void setupBefore() {
		
    	String dir = "C:\\Users\\Crespo\\eclipse-workspace\\devopsmetrics\\target\\cucumber"+dtf.format(now);

        try {

            Path path = Paths.get(dir);

            Files.createDirectories(path);

            System.out.println("Directory is created!");

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
	}
	
	@AfterClass
    public static void setupAfter() throws InterruptedException {
		
 	    Thread.sleep(1000);
    	File fJSONoriginal = new File("target/cucumber/TestReport.json");
    	File fJSONrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".json");
    	Thread.sleep(1000);
    	File fJUnitoriginal = new File("target/cucumber/TestReport.xml");
    	File fJUnitrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".xml");
    	Thread.sleep(1000);
    	File fHTMLoriginal = new File("target/cucumber/TestReport.html");
    	File fHTMLrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".html");
    	Thread.sleep(1000);
    	fJSONoriginal.renameTo(fJSONrenombrado);
    	Thread.sleep(1000);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	Thread.sleep(1000);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);
    	
    	
    	File folder = new File("target/cucumber");
    	Thread.sleep(1000);
    	folder.delete();
    	Thread.sleep(5000);
    }
	
    
}