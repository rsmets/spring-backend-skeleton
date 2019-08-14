package com.skeleton.project.facade.rest;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.QueryResponse;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.api.UserGroupResponse;
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

	/**
	 * @since 1.0
	 * @param userGroupRequest
	 * @return list of groups the targetPerson is an owner of
	 */
	@PostMapping("/v1.0/userGroup/getOwnerGroups")
	public List<UserGroup> getOwnerGroups(@RequestBody final UserGroupRequest userGroupRequest)
	{

		List<UserGroup> result = _coreEngine.getOwnerGroups(userGroupRequest);

		log.info("user " + userGroupRequest.getTargetPerson().getPrimaryEmail() + " is a group owner of groups: " + result.toString());

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

        log.info("new users add to group: " + result.toString());

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

		log.info("new admins add to group: " + result.toString());

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

		log.info("new locks add to group: " + result.toString());

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
	@PostMapping("/v1.0/userGroup/modifyDetails")
	public UserGroup modifyGroupDetails(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroup result = _coreEngine.modifyGroupDetails(userGroupRequest);

		log.info("user group with new details: " + result.toString());

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

		log.info("user group with new schedule: " + result.toString());

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

		log.info("user group with removed admins: " + result.toString());

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
	public UserGroupResponse removeSelf(@RequestBody final UserGroupRequest userGroupRequest)
	{
		UserGroupResponse result = _coreEngine.removeSelfFromGroup(userGroupRequest);

		log.info("removed (" + result + ") user, " + userGroupRequest.getTargetUsers() + ", from group " + userGroupRequest.getGroupId());

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
