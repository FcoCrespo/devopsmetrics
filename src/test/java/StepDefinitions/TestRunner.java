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
 	   	
    	String dir = "C:\\Users\\Crespo\\eclipse-workspace\\devopsmetrics\\target\\cucumber"+dtf.format(now);

        try {

            Path path = Paths.get(dir);

            Files.createDirectories(path);

            System.out.println("Directory is created!");

            //Files.createDirectory(path);

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    	
    	File fJSONoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber\\TestReport.json");
        //File fJSONoriginal = new File("target/cucumber/TestReport.json");
    	System.out.println("Hola");
    	if(fJSONoriginal.exists()) {
    		System.out.println("Existe");
    	}
    	else {
    		System.out.println("No Existe");
    	}
    	File fJSONrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".json");
    	
    	//File fJUnitoriginal = new File("target/cucumber/TestReport.xml");
    	File fJUnitoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber\\TestReport.xml");
    	File fJUnitrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".xml");
    	
    	//File fHTMLoriginal = new File("target/cucumber/TestReport.html");
    	File fHTMLoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber\\TestReport.html");
    	File fHTMLrenombrado = new File("target/cucumber"+dtf.format(now)+"/TestReport"+dtf.format(now)+".html");
    	
    	fJSONoriginal.renameTo(fJSONrenombrado);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);
    }
    
}