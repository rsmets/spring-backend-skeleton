package com.skeleton.project.core;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.exceptions.UserGroupPermissionsException;

import java.util.List;
import java.util.Set;

public interface ICoreEngine {

	/**
	 * Creates a new user group.
	 *
	 * NOTE: inflates the depenedent objs, but does not create them (that is handle in respective services,
	 * for not just in the main node app for things like Schedule, Key Relationships, etc)
	 * @param userGroup
	 * @return
	 */
	UserGroup createUserGroup(com.skeleton.project.dto.entity.UserGroup userGroup);

	/**
	 * Get the user group associated with the provided id.
	 * Note: no ownership validation, just a simple get
	 * @param id
	 * @return
	 */
	UserGroup getUserGroup(String id);

	/**
	 * Fetches a user group whilst doing validaiton that the requesting user has admin or owner access to the the group
	 * @param request
	 * @return
	 */
	UserGroup fetchOneUserGroup(UserGroupRequest request);

	/**
	 * Fetches user groups that the requestedUser belongs to and the requestingUser has has admin access to.
	 * NOTE: Accepts request that do not have requestedUser populated, which will be treated the same a request to just
	 * got the groups for the requestingUsers
	 * @param requestingUser
	 * @param requestedUsers
	 * @return
	 */
	Set<UserGroup> fetchUserGroups(User requestingUser, List<User> requestedUsers);

	/**
	 * Gets all user groups associated with a given user id of which they are an owner or and admin
	 * @param userId
	 * @return
	 */
	List<UserGroup> getUserGroupsForUser(String userId);

	/**
	 * Deletes the user group for the associated id. Internal obj deletion should
	 * be handled in their respective services, similar to creation
	 *
	 * @param id
	 * @return
	 */
	UserGroup deleteUserGroup(String id);

	/**
	 * Adds targetUsers of the UserGroupRequest to the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupPermissionsException
	 */
	UserGroup addUsersToGroup(UserGroupRequest request) throws UserGroupPermissionsException;

	//TODO: UserGroup addAdminsToGroup(UserGroupRequest request) throws UserGroupPermissionsException;

	/**
	 * Adds targetLocks of the UserGroupRequest to the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupPermissionsException
	 */
	UserGroup addLocksToGroup(UserGroupRequest request) throws UserGroupPermissionsException;

	/**
	 * Removes targetUsers of the UserGroupRequest from the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupPermissionsException
	 */
	UserGroup removeUsersFromGroup(UserGroupRequest request) throws UserGroupPermissionsException;

	/**
	 * Modifies group name. Request permissions validation performed.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupPermissionsException
	 */
	UserGroup modifyGroupName(UserGroupRequest request) throws UserGroupPermissionsException;

	/**
	 * Dummy function that should probably be deleted..
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);
}


