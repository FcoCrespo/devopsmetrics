package edu.uclm.esi.devopsmetrics.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import edu.uclm.esi.devopsmetrics.utilities.KeyValue;
import edu.uclm.esi.devopsmetrics.utilities.SleepClass;
import edu.uclm.esi.devopsmetrics.utilities.Utilities;


@CucumberContextConfiguration 
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features")
public class TestRunner {
    
}