package com.x.apa.common.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author liumeng
 */
@Component
public class RestClient {

	private static RestTemplate restTemplate;

	public RestClient(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.build();
	}
	
	public static RestTemplate getRestTemplate(){
		return restTemplate;
	}
}
