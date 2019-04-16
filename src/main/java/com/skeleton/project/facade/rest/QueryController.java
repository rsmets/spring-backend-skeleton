package com.skeleton.project.facade.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;
import com.skeleton.project.domain.*;
import com.skeleton.project.engine.ICoreEngine;
import com.skeleton.project.type.UserGroupType;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Api("QueryApi")
@Slf4j
public class QueryController {
	
	@Autowired
	private ICoreEngine _coreEngine;
	
	/**
	 * @since 0.0
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

	/**
	 * @since 1.0
	 * @param userGroup
	 * @return id of new UserGroup obj
	 */
	@PostMapping("/v1.0/create/userGroup")
	public String createUserGroup(@RequestBody final com.skeleton.project.dto.UserGroup userGroup)
	{
		UserGroup input = UserGroup.convertFromDto(userGroup);
		UserGroup result = _coreEngine.createUserGroup(input);
		log.info("new user group: " + result.toString());

		if (result == null)
			return "";

		return result.getId();
	}

	@GetMapping("/v1.0/get/userGroup/{id}")
	public UserGroup getUserGroup(@PathVariable String id) {
		UserGroup result = _coreEngine.getUserGroup(id);

		return result;
	}

	@DeleteMapping("/v1.0/delete/userGroup/{id}")
	public QueryResponse deleteUserGroup(@PathVariable String id) {
		//TODO
		return null;
	}

}
