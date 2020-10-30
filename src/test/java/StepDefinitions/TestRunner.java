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
plugin = {"json:target/reports/JSONReports/TestReport.json","pretty", "junit:target/reports/JUnitReports/TestReport.xml","html:target/reports/HTMLReports/TestReport.html"}
		)
public class TestRunner {

	final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
	final static LocalDateTime now = LocalDateTime.now();
	
	@BeforeClass	    
    public static void setupBefore() {
		
    	try {
    		 String dir = "C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/Reports-"+dtf.format(now);

    		  Path path = Paths.get(dir);

    		  //java.nio.file.Files;
    		  Files.createDirectories(path);

    		  System.out.println("Directory is created!");

    	} catch (IOException e) {

    		  System.err.println("Failed to create directory!" + e.getMessage());

    	}
		
		String source = "C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports";
		File srcDir = new File(source);

		String destination = "C:/Users/Crespo/eclipse-workspace/devopsmetrics/target/reports";
		File destDir = new File(destination);

		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
    @AfterClass	    
    public static void setupAfter() {	 
    	
    	  
    	
    	File fJSONoriginal = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/JSONReports/TestReport.json");
    	File fJSONrenombrado = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/Reports-"+dtf.format(now)+"/TestReport"+dtf.format(now)+".json");
    	
    	File fJUnitoriginal = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/JUnitReports/TestReport.xml");
    	File fJUnitrenombrado = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/Reports-"+dtf.format(now)+"/TestReport"+dtf.format(now)+".xml");
    	
    	File fHTMLoriginal = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/HTMLReports/TestReport.html");
    	File fHTMLrenombrado = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/Reports-"+dtf.format(now)+"/TestReport"+dtf.format(now)+".html");
    	
    	fJSONoriginal.renameTo(fJSONrenombrado);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);
    	
    	File folder = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/JSONReports");
    	folder.delete();
    	folder = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/JUnitReports");
    	folder.delete();
    	folder = new File("C:/Users/Crespo/.jenkins/workspace/devopsmetrics/target/reports/HTMLReports");
    	folder.delete();
    	
    	
    }
    
}