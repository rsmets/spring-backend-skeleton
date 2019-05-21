package com.skeleton.project.facade.rest;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.QueryResponse;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.core.ICoreEngine;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
	 * @return new UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/create")
	public UserGroup createUserGroup(@RequestBody final UserGroup userGroup)
	{
		UserGroup result = _coreEngine.createUserGroup(userGroup);
		log.info("new user group: " + result.toString());

		return result;
	}

	@GetMapping("/v1.0/userGroup/get/{id}")
	public UserGroup getUserGroup(@PathVariable String id) {
		UserGroup result = _coreEngine.getUserGroup(id);

		return result;
	}

	@GetMapping("/v1.0/userGroup/getForUser/{userId}")
	public List<UserGroup> getAllUserGroupsForUser(@PathVariable String userId) {
		List<com.skeleton.project.dto.entity.UserGroup>  result = _coreEngine.getUserGroupsForUser(userId, false);

		return result;
	}

	@PostMapping("/v1.0/userGroup/fetch")
	public Set<UserGroup> fetchUserGroups(@RequestBody UserGroupRequest request) {
//		Set<UserGroup> result = _coreEngine.fetchUserGroups(request.getRequestingUser(), request.getTargetUsers(), request.isAdministrativeAccessOnly());
		Set<UserGroup> result = _coreEngine.fetchUserGroups(request);

		return result;
	}

	@PostMapping("/v1.0/userGroup/fetchOne")
	public UserGroup fetchUserGroup(@RequestBody UserGroupRequest request) {
		UserGroup result = _coreEngine.fetchOneUserGroup(request);

		return result;
	}

	@PostMapping("/v1.0/userGroup/delete")
	public Set<KeyRelationship> deleteUserGroup(@RequestBody UserGroupRequest request) throws Exception {
		Set<KeyRelationship> result = _coreEngine.deleteUserGroup(request);
//		return QueryResponse.builder().example(result).build();
		return result;
	}

    /**
     * @since 1.0
     * @param userGroupRequest
     * @return new UserGroup obj
     */
    @PostMapping("/v1.0/userGroup/addUsers")
    public UserGroup addUsers(@RequestBody final UserGroupRequest userGroupRequest)
    {
        UserGroup result = _coreEngine.addUsersToGroup(userGroupRequest);

        log.info("new user group: " + result.toString());

        return result;
    }

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return new UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/addAdmins")
	public UserGroup addAdmins(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.addAdminsToGroup(userGroupRequest);

		log.info("new admins' group: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return new UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/addLocks")
	public UserGroup addLocks(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.addLocksToGroup(userGroupRequest);

		log.info("new user group: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/modifyName")
	public UserGroup modifyGroupName(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.modifyGroupName(userGroupRequest);

		log.info("user group with new name: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/modifySchedule")
	public UserGroup modifyGroupSchedule(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.modifyGroupSchedule(userGroupRequest);

		log.info("user group with new name: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/removeUsers")
	public UserGroup removeUsers(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.removeUsersFromGroup(userGroupRequest);

		log.info("user group with removed users: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/removeAdmins")
	public UserGroup removeAdmins(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.removeAdminsFromGroup(userGroupRequest);

		log.info("user group with removed users: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/removeLocks")
	public UserGroup removeLocks(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.removeLocksFromGroup(userGroupRequest);

		log.info("user group with removed locks: " + result.toString());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/removeSelf")
	public UserGroup removeSelf(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.removeSelfFromGroup(userGroupRequest);

		log.info("removed users " + userGroupRequest.getTargetUsers() + " from group " + userGroupRequest.getGroupId());

		return result;
	}

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return modified UserGroup obj
	 */
	@PostMapping("/v1.0/userGroup/getUsersKeyRelationships")
	public Set<KeyRelationship> getUsersKeyRelationships(@RequestBody final UserGroupRequest userGroupRequest)
	{
		Set<KeyRelationship> result = _coreEngine.getGroupKeyRelationshipsForUsers(userGroupRequest);

		log.info("user group key relationships for users: " + result.toString());

		return result;
	}

}
