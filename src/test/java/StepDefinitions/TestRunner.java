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

	@BeforeClass
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
    	
    	File fJSONoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber");
        //File fJSONoriginal = new File("target/cucumber/TestReport.json");
    	File fJSONrenombrado = new File("C:\\Users\\Crespo\\eclipse-workspace\\devopsmetrics\\target\\cucumber"+dtf.format(now));
    	
    	//File fJUnitoriginal = new File("target/cucumber/TestReport.xml");
    	File fJUnitoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber");
    	File fJUnitrenombrado = new File("C:\\Users\\Crespo\\eclipse-workspace\\devopsmetrics\\target\\cucumber"+dtf.format(now));
    	
    	//File fHTMLoriginal = new File("target/cucumber/TestReport.html");
    	File fHTMLoriginal = new File("C:\\Users\\Crespo\\.jenkins\\workspace\\devopsmetrics\\target\\cucumber");
    	File fHTMLrenombrado = new File("C:\\Users\\Crespo\\eclipse-workspace\\devopsmetrics\\target\\cucumber"+dtf.format(now));
    	
    	
    	try {
    		Thread.sleep(1000);
			FileUtils.copyDirectory(fJSONoriginal,fJSONrenombrado);
			Thread.sleep(1000);
			FileUtils.copyDirectory(fJUnitoriginal,fJUnitrenombrado);
			Thread.sleep(1000);
			FileUtils.copyDirectory(fHTMLoriginal,fHTMLrenombrado);
			Thread.sleep(1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/*fJSONoriginal.renameTo(fJSONrenombrado);
    	fJUnitoriginal.renameTo(fJUnitrenombrado);
    	fHTMLoriginal.renameTo(fHTMLrenombrado);*/ 
    	catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}