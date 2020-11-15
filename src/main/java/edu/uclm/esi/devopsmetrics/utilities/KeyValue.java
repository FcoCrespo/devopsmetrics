package edu.uclm.esi.devopsmetrics.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyValue {

	@Value("${app.key}")
	private String accessKey;
	
	@Value("${github.token}")
	private String tokenFcoCrespo;
	
	@Value("${github.tokenesalud}")
	private String tokenSherrerap;

	public String getSecret() {
	   return this.accessKey;
	}
	
	public String getTokenFcoCrespo() {
		return  this.tokenFcoCrespo;
	}
	
	public String getTokenSherrerap() {
		return  this.tokenSherrerap;
	}

}