package com.skeleton.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.core.DatabaseDriver;
import com.skeleton.project.core.ICoreEngine;
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
import java.util.*;

@Service
@Slf4j
public class CoreEngine implements ICoreEngine {

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

		// inflate the user list (this generally if coming from the stand along request to add users to group)
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
		}

		// create the key relationship mapping
		Map<String, List<KeyRelationship>> keyRelationshipMap = createKeyRelationshipMapping(request.getKeyRelationships());

		return userGroupService.additiveGroupModification(group, request.getTargetUsers(), request.getKeyRelationships(), request.getTargetLockIds(), keyRelationshipMap);
	}

	@Override
	public UserGroup addLocksToGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// create the key relationship mapping
		Map<String, List<KeyRelationship>> keyRelationshipMap = createKeyRelationshipMapping(request.getKeyRelationships());

		return userGroupService.additiveGroupModification(group, Collections.emptyList(), request.getKeyRelationships(), request.getTargetLockIds(), keyRelationshipMap);
	}

	@Override
	public UserGroup removeUsersFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// Need to grab all the key relationships for said user, delete them, and remove them from group
		Set<KeyRelationship> userGroupKrs = new HashSet<>();
		for(User user : request.getTargetUsers()) {
			userGroupKrs.addAll(group.getKeyRelationshipsMap().get(user.getId()));

			// remove user from the key relationship map
			group.getKeyRelationshipsMap().remove(user.getId());
		}

		// Actually do NOT want to delete krs from the group service due to not having triggering the db hooks that currently only live in parse world
//		for(KeyRelationship keyRelationship: userGroupKrs) {
//			keyRelationshipService.deleteKeyRelationship(keyRelationship.getId()); // TODO figure out a batch delete method
//		}

		return userGroupService.reductiveGroupModification(group, request.getTargetUsers(), userGroupKrs, Collections.emptyList(), group.getKeyRelationshipsMap());
	}

	@Override
	public UserGroup removeLocksFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// Need to grab all the key relationships for said user and remove them from group. Deletion taking place in the key relationship service (aka parse) so the db hooks can fire
		Set<KeyRelationship> keyRelationshipsToRemove = new HashSet<>();
		Map<String, List<KeyRelationship>> krsMap = new HashMap<>(group.getKeyRelationshipsMap()); // deep copy so can modify without concurrent modification exception
		for (Map.Entry<String, List<KeyRelationship>> entry : group.getKeyRelationshipsMap().entrySet()) {
			for(KeyRelationship kr : entry.getValue()) {
				if(request.getTargetLockIds().contains(kr.getKey().getId())) {
					// found a kr that needs to be removed
					keyRelationshipsToRemove.add(kr);
					krsMap.get(kr.getUser().getId()).remove(kr);
				}
			}
		}

		group.getKeyRelationships().removeAll(keyRelationshipsToRemove);

		return userGroupService.reductiveGroupModification(group, Collections.emptyList(), group.getKeyRelationships(), request.getTargetLockIds(), krsMap);
//		return userGroupService.reductiveGroupModification(group, Collections.emptyList(), group.getKeyRelationships(), request.getTargetLockIds(), group.getKeyRelationshipsMap());
	}

	@Override
	public UserGroup removeSelfFromGroup(UserGroupRequest request) throws UserGroupAdminPermissionsException {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation: special in the sense if the user is acting on his self it is valid too
		verifyUserRequest(request, group);

		// Need to grab all the key relationships for said user, delete them, and remove them from group
		User user = request.getTargetUsers().get(0); // should be singular if not something is wrong... todo throw exception
		Set<KeyRelationship> userGroupKrs = new HashSet<>(keyRelationshipService.getKeyRelationshipsByUserAndGroup(user.getId(), request.getGroupId()));

		// Actually do NOT want to delete krs from the group service due to not having triggering the db hooks that currently only live in parse world
//		for(KeyRelationship keyRelationship: userGroupKrs) {
//			keyRelationshipService.deleteKeyRelationship(keyRelationship.getId()); // TODO figure out a batch delete method
//		}

		group.getKeyRelationshipsMap().remove(user.getId());

		return userGroupService.reductiveGroupModification(group, request.getTargetUsers(), userGroupKrs, Collections.emptyList(), group.getKeyRelationshipsMap());
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
	public Set<KeyRelationship> getGroupKeyRelationshipsForUsers(UserGroupRequest request) {
		UserGroup group = userGroupService.getUserGroup(request.getGroupId());
		// verify a valid operation
		verifyRequest(request, group);

		// Need to grab all the key relationships for said user and remove them from group. The delete should be handled in the KR service (aka parse land) due to db hooks
		Set<KeyRelationship> userGroupKrs = new HashSet<>();
		for(User user : request.getTargetUsers()) {
			userGroupKrs.addAll(group.getKeyRelationshipsMap().get(user.getId()));
		}

		return userGroupKrs;
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

	private Map<String, List<KeyRelationship>> createKeyRelationshipMapping(Set<KeyRelationship> keyRelationships) {
		// create the key relationship mapping
		Map<String, List<KeyRelationship>> keyRelationshipMap = new HashMap<>();
		for (KeyRelationship kr : keyRelationships) {
			String userId = kr.getUser().getId();
			List<KeyRelationship> krs = keyRelationshipMap.get(userId);

			if (krs == null)
				krs = new ArrayList<>();

			krs.add(kr);
			keyRelationshipMap.put(userId, krs);
		}

		return keyRelationshipMap;
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
