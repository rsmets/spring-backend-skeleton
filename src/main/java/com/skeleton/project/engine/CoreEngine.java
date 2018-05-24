package com.skeleton.project.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skeleton.project.facade.rest.IClient;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skeleton.project.domain.BaseResponse;

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
			log.error("That request did not work... ");
		}

		return new BaseResponse();

	}
}
