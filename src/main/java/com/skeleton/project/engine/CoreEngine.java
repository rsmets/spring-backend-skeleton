package com.skeleton.project.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skeleton.project.facade.rest.IClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skeleton.project.domain.BaseResponse;

@Service
public class CoreEngine implements ICoreEngine{

	@Autowired
	IClient _client;


	@Override
	public List<BaseResponse> executeAction(Object example) {
		BaseResponse response = _client.doAction(example);

		// rainbows and unicorns

		return new ArrayList<>(Arrays.asList(response));
	}
}
