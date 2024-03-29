package edu.uclm.esi.devopsmetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
*
* @author FcoCrespo
* devopsmetrics-backend
*/
@SpringBootApplication
@EnableScheduling 
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
}