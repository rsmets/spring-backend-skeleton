package com.skeleton.project.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Lock;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.exceptions.LockAdminPermissionsException;
import com.skeleton.project.exceptions.ModifcationException;
import com.skeleton.project.exceptions.UserGroupAdminPermissionsException;
import com.skeleton.project.facade.rest.IClient;
import com.skeleton.project.jackson.UserDeserializer;
import com.skeleton.project.service.IKeyRelationshipService;
import com.skeleton.project.service.ILockService;
import com.skeleton.project.service.IUserGroupService;
import com.skeleton.project.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CoreEngine implements ICoreEngine{

	@Autowired
	IClient _client;
	@Autowired
	DatabaseDriver database;
	@Autowired
	IUserGroupService userGroupService;
	@Autowired
	IUserService userService;
	@Autowired
	IKeyRelationshipService keyRelationshipService;
	@Autowired
    ILockService lockService;

	/**
	 * @see ICoreEngine#createUserGroup(UserGroup)
	 */
	@Override
	public UserGroup createUserGroup(UserGroup userGroup) {

		verifyOwnerRequest(userGroup);
		return userGroupService.createUserGroup(userGroup);
	}

	/**
	 * @see ICoreEngine#getUserGroup(String)
	 */
	@Override
	public UserGroup getUserGroup(final String id) {
		return userGroupService.getUserGroup(id);
	}

	@Override
	public UserGroup fetchOneUserGroup(UserGroupRequest request) {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());

		verifyRequest(request, group);
		return group;
	}

	/**
	 * @see ICoreEngine#fetchUserGroups(User, List)
	 */
	@Override
	public Set<UserGroup> fetchUserGroups(User requestingUser, List<User> requestedUsers) {

		// iff the requestedUser list is empty this means that the request is just meant to grab the requesting user's groups
		if (requestedUsers == null || requestedUsers.isEmpty()) {
			return new HashSet<>(userGroupService.getUserGroupsForUser(requestingUser.getId()));
		}

		List<UserGroup> requestingUsersGroups = userGroupService.getUserGroupsForUser(requestingUser.getId());

		Set<UserGroup> requestedUsersGroups = new HashSet<>();
		for(User user : requestedUsers) {
			requestedUsersGroups.addAll(userGroupService.getUserGroupsForUser(user.getId()));
		}

		requestedUsersGroups.retainAll(requestingUsersGroups);

		return requestedUsersGroups;
	}

	/**
	 * @see ICoreEngine#getUserGroupsForUser(String)
	 */
	@Override
	public List<UserGroup> getUserGroupsForUser(final String userId) {
		return userGroupService.getUserGroupsForUser(userId);
	}

	/**
	 * @see ICoreEngine#deleteUserGroup(String)
	 */
	@Override
	public WriteResult deleteUserGroup(final String id) {
		WriteResult result = userGroupService.deleteUserGroup(id);

		return result;
	}

	/**
	 * @see ICoreEngine#addUsersToGroup(UserGroupRequest)
	 */
    @Override
    public UserGroup addUsersToGroup(final UserGroupRequest request) throws UserGroupAdminPermissionsException {

    	UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// inflate the user list
		if (request.isNeedToInflate()) {
			List<User> inflatedUsers = new ArrayList<>();
			for (User user : request.getTargetUsers()) {
				if (user.getPrimaryPhone() != null)
					inflatedUsers.add(userService.getUserByPhone(user.getPrimaryPhone()));
				else if (user.getPrimaryEmail() != null)
					inflatedUsers.add(userService.getUserByEmail(user.getPrimaryEmail()));
				else
					log.warn("No user identifier provided in " + request);
			}
			request.setTargetUsers(inflatedUsers);

//			// TODO need to inflate the kr list too! Do I though?? I think not, only having the objectId is suffice.
//			List<KeyRelationship> inflatedKRs = new ArrayList<>();
//			for (KeyRelationship KeyRelationship : request.getKeyRelationships()) {
//				inflatedKRs.add(keyRelationshipService.getKeyRelationship(KeyRelationship.getId()));
//			}
//			request.setKeyRelationships(inflatedKRs);

		}

		return userGroupService.additiveGroupModification(group, request.getTargetUsers(), request.getKeyRelationships(), request.getTargetLockIds());
    }

	@Override
	public UserGroup addLocksToGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		return userGroupService.additiveGroupModification(group, Collections.emptyList(), request.getKeyRelationships(), request.getTargetLockIds());
	}

	@Override
	public UserGroup removeUsersFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// TODO Need to grab all the key relationships for said user and remove them from group (ALSO NEED TO DELETE THEM... HMM TRUE KR SERVICE IMPL??)
		List<KeyRelationship> userGroupKrs = new ArrayList<>();
		for(User user : request.getTargetUsers()) {
			userGroupKrs.addAll(keyRelationshipService.getKeyRelationshipsByUserAndGroup(user.getId(), request.getGroupId()));
		}

		return userGroupService.reductiveGroupModification(group, request.getTargetUsers(), userGroupKrs, Collections.emptyList());
	}

	@Override
	public UserGroup removeSelfFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation: special in the sense if the user is acting on his self it is valid too
		verifyUserRequest(request, group);

		// TODO Need to grab all the key relationships for said user and remove them
		// yikes do not have that info handy!

		return userGroupService.reductiveGroupModification(group, request.getTargetUsers(), request.getKeyRelationships(), Collections.emptyList());
	}

	@Override
	public UserGroup modifyGroupName(UserGroupRequest request) throws UserGroupAdminPermissionsException {

		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		return userGroupService.modifyGroupName(group, request.getNewGroupName());
	}

	@Override
	public UserGroup removeKeyRelationships(UserGroupRequest request) {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());

		return userGroupService.removeKeyRelationships(group, request.getKeyRelationships());
	}

	@Override
	public BaseResponse executeAction(final Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			com.skeleton.project.domain.User fullUser = (com.skeleton.project.domain.User) getDbFullObject(example);

			KeyRelationship kr = keyRelationshipService.getKeyRelationship("3dy7V2SSoN");


			return BaseResponse.builder().example(kr).build();
		} catch (Exception e) {
			log.error("That request did not work... ", e);
		}

		return null;

	}

	/**
	 * Verifies that the group owner has the proper key relationship with group's locks
	 * @param group
	 * @throws UserGroupAdminPermissionsException
	 * @throws EntityNotFoundException
	 */
	private void verifyOwnerRequest(UserGroup group) throws LockAdminPermissionsException, EntityNotFoundException {
		for (String lockId : group.getLockIds()) {

			Lock lock = lockService.getLockByLockId(lockId);
			com.skeleton.project.dto.entity.KeyRelationship ownersLockKeyRelationship = keyRelationshipService.getKeyRelationship(group.getOwner().getId(), lock.getId());

			if (ownersLockKeyRelationship == null) {
				log.error("The attempted group owner does not have admin+ access to that lock");
				throw new LockAdminPermissionsException(lockId, group.getOwner().getId());
			}
			//RJS due to the Role pointer being a string would need to TODO roleService todo db retrieval todo roleId comparison here. Will do in node world for now
//        else if (ownersLockKeyRelationship.getRole() == null || ownersLockKeyRelationship.getRole().getRoleId() == 0) {
//            log.error("The attempted group owner does not have admin privileges to that lock");
//            // TODO send back a custom exception
//            return null;
//        }
		}
	}

	/**
	 * Verifies the requesting user has admin or owner permissions and throws exception if not
	 * @param request
	 * @param group
	 */
	private void verifyRequest(UserGroupRequest request, UserGroup group) throws UserGroupAdminPermissionsException, EntityNotFoundException {

		if (group == null) {
			String message = "Requested group with id " + request.getGroupId() + " does not exist";
			log.error(message);
			throw new EntityNotFoundException(message);
		}

		if (!doesUserHaveAdminRights(request.getRequestingUser().getId(), group)) {
			log.error("Requesting user " + request.getRequestingUser().getId() + " does not have admin privileges for group " + group.getName() + " " + group.getId());
			throw new UserGroupAdminPermissionsException(request.getRequestingUser().getId(), group.getId(), group.getName());
		}
	}

	/**
	 * Verifies the requesting user is acting only on oneself and throws exception if not
	 * @param request
	 * @param group
	 */
	private void verifyUserRequest(UserGroupRequest request, UserGroup group) throws UserGroupAdminPermissionsException, EntityNotFoundException {

		if (group == null) {
			String message = "Requested group with id " + request.getGroupId() + " does not exist";
			log.error(message);
			throw new EntityNotFoundException(message);
		}

		if (!isUserActingOnSelf(request)) {
			log.error("Requesting user " + request.getRequestingUser().getId() + " does not have admin privileges for group " + group.getName() + " " + group.getId());
			throw new ModifcationException(request.getRequestingUser().getId(), group.getId(), group.getName());
		}
	}

	/**
	 * Checks if the user id is in the owner or admin list.
	 * @param requestingUserId
	 * @param group
	 * @return
	 */
	private boolean doesUserHaveAdminRights(String requestingUserId, UserGroup group) {
		if ( StringUtils.equals(group.getOwner().getId(), requestingUserId) ) {
			return true;
		}

		// I know there are 'sexier' ways than this... but I do have a love for the readability of the for loop
		for(User admins : group.getAdmins()) {
			if ( StringUtils.equals(admins.getId(), requestingUserId) ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the requesting user id matches targetUser's
	 * @param request
	 * @return
	 */
	private boolean isUserActingOnSelf(UserGroupRequest request) {
		for (User user : request.getTargetUsers()) {
			if ( !StringUtils.equals(request.getRequestingUser().getId(), user.getId()) ) {
				return false;
			}
		}

		return true;
	}

	private Object getDbFullObject(final Object search) {
		MongoCollection<Document>  userCollection = database.getDatabase().getCollection("_User");

		Document doc = userCollection.find().first();
		log.info("doc from db: " + doc.toJson());

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(com.skeleton.project.domain.User.class, new UserDeserializer());
		mapper.registerModule(module);

		com.skeleton.project.domain.User user = null;
		try {
			user = mapper.readValue(doc.toJson(), com.skeleton.project.domain.User.class);
			log.info("doc from jacksonified db: " + user.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return user;
	}
}
