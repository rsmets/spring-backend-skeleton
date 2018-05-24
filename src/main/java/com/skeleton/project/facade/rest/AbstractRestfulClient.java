package com.skeleton.project.facade.rest;

import org.springframework.web.client.RestTemplate;

public abstract class AbstractRestfulClient {
	protected RestTemplate _restTemplate = new RestTemplate();
}
