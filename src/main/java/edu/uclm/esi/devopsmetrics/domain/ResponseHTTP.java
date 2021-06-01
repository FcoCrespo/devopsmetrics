package edu.uclm.esi.devopsmetrics.domain;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import edu.uclm.esi.devopsmetrics.utilities.KeyValue;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Service
public class ResponseHTTP {
	
	
	private final KeyValue keyvalue;
	private String token;
	
	public ResponseHTTP(final KeyValue keyvalue) {
		this.keyvalue = keyvalue;
		this.token = "";
	}
	
	public Response prepareResponse(final String graphqlPayload, final String graphqlUri, final String owner) {
		
		if(owner.equals("FcoCrespo")){
			this.token = this.keyvalue.getTokenFcoCrespo();
		}
		else {
			this.token = this.keyvalue.getTokenSherrerap();
		}
		
		
		OkHttpClient clientHTTP = new OkHttpClient().newBuilder()
	            .connectTimeout(5, TimeUnit.MINUTES) 
	            .writeTimeout(5, TimeUnit.MINUTES) 
	            .readTimeout(5, TimeUnit.MINUTES) 
	            .build();
	
		RequestBody body = RequestBody.create(graphqlPayload, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
        		.url(graphqlUri)
        		.addHeader("Authorization", "Bearer " + this.token )
        		.post(body)
        		.build();
        Response response;
		try {
			response = clientHTTP.newCall(request).execute();
			 if(response.isSuccessful()){
		            return response;             
		     }else return null;
		} catch (IOException e) {
			return null;
		}
	
	}

}
