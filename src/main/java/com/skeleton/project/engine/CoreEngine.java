package com.skeleton.project.engine;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.facade.rest.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoreEngine implements ICoreEngine{

	@Autowired
	IClient _client;


	@Override
	public BaseResponse executeAction(Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			// rainbows and unicorns

			return response;

		} catch (Exception e) {
			// probably want to do something clever here.
			log.error("That request did not work... ", e);

		}

		return null;

	}
}
