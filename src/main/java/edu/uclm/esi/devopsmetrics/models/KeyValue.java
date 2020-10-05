package edu.uclm.esi.devopsmetrics.models;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// Autowired in static methods : t.ly/hR2b
@Component
public class KeyValue {

	@Autowired
	private Environment env;
	
	private static String secretKey; 

	@PostConstruct
	public void init(){
		secretKey = env.getProperty("app.key"); 
	}
	
	public static String getSecret() {
		return secretKey;
	}

}