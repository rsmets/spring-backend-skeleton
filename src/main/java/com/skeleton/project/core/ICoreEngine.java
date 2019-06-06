package com.skeleton.project.core;

import com.mongodb.WriteResult;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.exceptions.ModifcationException;
import com.skeleton.project.exceptions.UserGroupAdminPermissionsException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

public interface ICoreEngine {

	/**
	 * Creates a new user group.
	 *
	 * NOTE: inflates the dependent objs, but does not create them (that is handle in respective services,
	 * for not just in the main node app for things like Schedule, Key Relationships, etc)
	 * @param userGroup
	 * @return
	 */
	UserGroup createUserGroup(com.skeleton.project.dto.entity.UserGroup userGroup);

	/**
	 * Deletes the user group for the associated id. Internal obj deletion should
	 * be handled in their respective services, similar to creation
	 *
	 * @param request
	 * @return set of the group's key relationships
	 */
	Set<KeyRelationship> deleteUserGroup(UserGroupRequest request) throws Exception;

	/**
	 * Get the user group associated with the provided id.
	 * Note: no ownership validation, just a simple get
	 * @param id
	 * @return
	 */
	UserGroup getUserGroup(String id);

	/**
	 * Fetches a user group whilst doing validation that the requesting user has admin or owner access to the the group
	 * @param request
	 * @return
	 */
	UserGroup fetchOneUserGroup(UserGroupRequest request);

	/**
	 * Fetches user groups that the requestedUser belongs to and the requestingUser has has admin access to.
	 * NOTE: Accepts request that do not have requestedUser populated, which will be treated the same a request to just
	 * get the groups for the requestingUsers
	 * @param requestingUser
	 * @param requestedUsers
	 * @return
	 */
	Set<UserGroup> fetchUserGroups(User requestingUser, List<User> requestedUsers, boolean administrativeAccessOnly);
	Set<UserGroup> fetchUserGroups(UserGroupRequest request);

	/**
	 * Gets all user groups associated with a given user id of which they are an owner or and admin
	 * @param userId
	 * @return
	 */
	List<UserGroup> getUserGroupsForUser(String userId, boolean administrativeAccessOnly);

	/**
	 * Adds targetUsers of the UserGroupRequest to the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup addUsersToGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Adds targetAdmins of the UserGroupRequest to the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup addAdminsToGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Adds targetLocks of the UserGroupRequest to the provided user group id. Including key relationships.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup addLocksToGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Removes targetUsers of the UserGroupRequest from the provided user group id. Including key relationships.
	 * Validation is required for this batch operation.
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup removeUsersFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Removes targetUsers of the UserGroupRequest from the provided user group id. Including key relationships.
	 * Validation is required for this batch operation.
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup removeAdminsFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Removes locks from the provided user group id. Including key relationships.
	 * Validation is required for this batch operation.
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup removeLocksFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Removes a user (and their key relationships) from the specified group.
	 * Note special validation: valid if owner, admin or group OR acting on oneself (removing oneself from group)
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup removeSelfFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException, EntityNotFoundException, ModifcationException;

	/**
	 * Modifies group name. Request permissions validation performed.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup modifyGroupName(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Modifies group details. Request permissions validation performed.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup modifyGroupDetails(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Modifies group name. Request permissions validation performed.
	 *
	 * @param request
	 * @return
	 * @throws UserGroupAdminPermissionsException
	 */
	UserGroup modifyGroupSchedule(UserGroupRequest request) throws UserGroupAdminPermissionsException;

	/**
	 * Removes key relationship from group. NO request validation performed.
	 * TODO: figure out a secret auth for these requests that have no requestingUser
	 *
	 * @param request
	 * @return
	 */
	UserGroup removeKeyRelationships(UserGroupRequest request);

	/**
	 * Grabs the key relationships corresponding to the group and users list
	 * @param request
	 * @return
	 */
	Set<KeyRelationship> getGroupKeyRelationshipsForUsers(UserGroupRequest request);

	/**
	 * Dummy function that should probably be deleted..
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);
}


