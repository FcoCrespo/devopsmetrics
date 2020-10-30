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
plugin = {"json:target/JSONReports/TestReport.json","pretty", "junit:target/JUnitReports/TestReport.xml","html:target/HTMLReports/TestReport.html"}
		)
public class TestRunner {

	
    @AfterClass	    
    public static void setupAfter() {	 
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
    	LocalDateTime now = LocalDateTime.now();  
    	
    	File fJSONoriginal = new File("target/JSONReports/TestReport.json");
    	File fJSONrenombrado = new File("target/reports/Reports"+dtf.format(now)+"JSONReports/TestReport"+dtf.format(now)+".json");
    	
    	File fJUnitoriginal = new File("target/reports/Reports/"+dtf.format(now)+"JUnitReports/TestReport.xml");
    	File fJUnitrenombrado = new File("target/JUnitReports/TestReport"+dtf.format(now)+".xml");
    	
    	File fHTMLoriginal = new File("target/reports/Reports/"+dtf.format(now)+"HTMLReports/TestReport.html");
    	File fHTMLrenombrado = new File("target/HTMLReports/TestReport"+dtf.format(now)+".html");
    	
    	fJSONoriginal.renameTo(fJSONrenombrado);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);
    }
    
}