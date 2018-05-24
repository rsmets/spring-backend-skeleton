package com.skeleton.project.facade.rest;

import java.util.ArrayList;
import java.util.List;

import com.skeleton.project.domain.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skeleton.project.engine.ICoreEngine;

@RestController
public class QueryController {
	
	@Autowired
	private ICoreEngine _coreEngine;
	
	/**
	 * @since 1.0
	 * @param search
	 * @return List of {@QueryResponse}
	 */
	@RequestMapping(value = "/example", method = RequestMethod.GET, produces = "application/json")
	public List<QueryResponse> query(@RequestParam(value="search", defaultValue="BallmerPeak") final Object search)
	{
		List<QueryResponse> result = new ArrayList<QueryResponse>();
		
		// oh look a dog...
		
		return result;
	}
}
