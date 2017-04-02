package com.cts.osp.ems.client;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.cts.osp.ems.controller.EmployeeConstants;

public class EmployeeClient {
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeClient.class);
	
	public static void main(String[] args) throws ParseException{
		
		
		
		 final String uri = EmployeeConstants.EMP_URI;
	        
	        final String body = "<employee> <id>234</id> <name>alan</name> <joiningDate>2002-09-24</joiningDate> <department>hr</department> </employee>";
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_XML);
	        
	        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
	        
	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.postForEntity(uri, entity, String.class).getBody();
	        LOGGER.info("The Response Object is "+result);
	        
	    }
}