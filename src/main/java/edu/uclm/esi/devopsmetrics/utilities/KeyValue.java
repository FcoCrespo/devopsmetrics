package edu.uclm.esi.devopsmetrics.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyValue {

	@Value("${app.key}")
	private String accessKey;

	public String getSecret() {
	   return this.accessKey;
	}

}