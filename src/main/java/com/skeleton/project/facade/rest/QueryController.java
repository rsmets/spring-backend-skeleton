package com.skeleton.project.facade.rest;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.QueryResponse;
import com.skeleton.project.engine.ICoreEngine;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("QueryApi")
public class QueryController {
	
	@Autowired
	private ICoreEngine _coreEngine;
	
	/**
	 * @since 1.0
	 * @param search
	 * @return List of {@QueryResponse}
	 */
	@RequestMapping(value = "/v1.0/example", method = RequestMethod.GET, produces = "application/json")
	public QueryResponse query(@RequestParam(value="search", defaultValue="BallmerPeak") final Object search)
	{
		BaseResponse baseResponse = _coreEngine.executeAction(search);

		// oh look a dog...

		QueryResponse result = QueryResponse.builder()
				.example(baseResponse.getExample())
				.build();

		return result;
	}
}
