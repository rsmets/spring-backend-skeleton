package com.skeleton.project.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.domain.*;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.exceptions.UserGroupPermissionsException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public com.skeleton.project.dto.entity.UserGroup createUserGroup(com.skeleton.project.dto.entity.UserGroup userGroup) {

	    com.skeleton.project.dto.entity.Lock lock = lockService.getLockByLockId(userGroup.getLockIds().get(0));
        com.skeleton.project.dto.entity.KeyRelationship ownersLockKeyRelationship = keyRelationshipService.getKeyRelationship(userGroup.getOwner().getId(), lock.getId());

        if(ownersLockKeyRelationship == null) {
            log.error("The attempted group owner does not have access to that lock");
            // TODO send back a custom exception
            return null;
        }
        //RJS due to the Role pointer being a string would need to TODO roleService todo db retrieval todo roleId comparison here. Will do in node world for now
//        else if (ownersLockKeyRelationship.getRole() == null || ownersLockKeyRelationship.getRole().getRoleId() == 0) {
//            log.error("The attempted group owner does not have admin privileges to that lock");
//            // TODO send back a custom exception
//            return null;
//        }

		return userGroupService.createUserGroup(userGroup);
	}

	@Override
	public com.skeleton.project.dto.entity.UserGroup getUserGroup(String id) {
		return userGroupService.getUserGroup(id);
	}

	@Override
	public UserGroup deleteUserGroup(String id) {
//		return userGroupService.deleteUser(id);

		//TODO
		return null;
	}

    @Override
    public com.skeleton.project.dto.entity.UserGroup addUsersToGroup(UserGroupRequest request) throws UserGroupPermissionsException {
		// verify a valid operation
		com.skeleton.project.dto.entity.UserGroup group = userGroupService.getUserGroup(request.getGroupId());

		if (!StringUtils.equals(group.getOwner().getId(), request.getRequestingUser().getId())) {
			log.error("Requesting user " + request.getRequestingUser().getId() + " does not have admin privileges for group " + group.getName() + " " + group.getId());
			throw new UserGroupPermissionsException(request.getRequestingUser().getId(), group.getId(), group.getName());
		}

		// inflate the user list
		if (request.isNeedToInflate()) {
			List<com.skeleton.project.dto.entity.User> inflatedUsers = new ArrayList<>();
			for (com.skeleton.project.dto.entity.User user : request.getTargetUsers()) {
				if (user.getPrimaryPhone() != null)
					inflatedUsers.add(userService.getUserByPhone(user.getPrimaryPhone()));
				else if (user.getPrimaryEmail() != null)
					inflatedUsers.add(userService.getUserByEmail(user.getPrimaryEmail()));
				else
					log.warn("No user identifier provided in " + request);
			}

			request.setTargetUsers(inflatedUsers);
		}

        return userGroupService.addUsers(group, request.getTargetUsers());
    }

    @Override
	public BaseResponse executeAction(Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			com.skeleton.project.domain.User fullUser = (com.skeleton.project.domain.User) getDbFullObject(example);

//			insertAndGrabUserGroupObject(example);
//			insertAndGrabKeyRelationshipObject(example);

//			User user = userService.getUser("3l6FvM305C");
//			UserGroup userGroup = userGroupService.getUserGroup("5ca6d2211f093865027e93db");
			com.skeleton.project.dto.entity.KeyRelationship kr = keyRelationshipService.getKeyRelationship("3dy7V2SSoN");


			return BaseResponse.builder().example(kr).build();
//			return null;
		} catch (Exception e) {
			log.error("That request did not work... ", e);
		}

		return null;

	}

//	private User getUser(String objId) {
//		return userService.getUser(objId);
//	}

	private void insertAndGrabUserGroupObject(Object obj){
		UserGroup userGroup = UserGroup.builder().canRemoteUnlock(true).canUnlockUntil(true).build();
		userGroup.setName((String)obj);

//		UserGroup newObj = userGroupService.createUserGroup(userGroup);
//		userGroupService.getUserGroup(newObj.getId().toHexString());
	}

//	private void insertAndGrabKeyRelationshipObject(Object obj){
//		KeyRelationship kr = KeyRelationship.builder().repeatInterval(1).repeatType(2).expirationDateUses(true).build();
//
//		keyRelationshipService.createKeyRelationship(kr);
//
//	}

	private Object getDbFullObject(Object search) {
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
