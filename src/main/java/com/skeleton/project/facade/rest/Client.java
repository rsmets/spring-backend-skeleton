package com.skeleton.project.facade.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.skeleton.project.domain.BaseResponse;

@Service
public class Client extends AbstractRestfulClient implements IClient {
		
	@Value("${api.key}")
	private String _apiKey;
	
	@Value("${api.base}")
	private String _baseUrl; 
	
	@Value("${api.action.path}")
	private String _actionPath;

	@Override
	public BaseResponse doAction(Object example) {
		String url = _baseUrl + _actionPath + example;

		BaseResponse response = _restTemplate.getForObject(url, BaseResponse.class);
		
		return response;
	}
	
}
