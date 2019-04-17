package com.skeleton.project.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.domain.*;
import com.skeleton.project.facade.rest.IClient;
import com.skeleton.project.jackson.UserDeserializer;
import com.skeleton.project.service.IKeyRelationshipService;
import com.skeleton.project.service.ILockService;
import com.skeleton.project.service.IUserGroupService;
import com.skeleton.project.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
	public UserGroup createUserGroup(UserGroup userGroup) {

	    Lock lock = lockService.getLockByLockId(userGroup.getLockIds().get(0));
        KeyRelationship ownersLockKeyRelationship = keyRelationshipService.getKeyRelationship(userGroup.getOwner().getId(), lock.getId());

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
	public UserGroup getUserGroup(String id) {
		return userGroupService.getUserGroup(id);
	}

	@Override
	public UserGroup deleteUserGroup(String id) {
//		return userGroupService.deleteUser(id);

		//TODO
		return null;
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
			KeyRelationship kr = keyRelationshipService.getKeyRelationship("3dy7V2SSoN");


			return BaseResponse.builder().example(kr).build();
//			return null;
		} catch (Exception e) {
			log.error("That request did not work... ", e);
		}

		return null;

	}

	private User getUser(String objId) {
		return userService.getUser(objId);
	}

	private void insertAndGrabUserGroupObject(Object obj){
		UserGroup userGroup = UserGroup.builder().canRemoteUnlock(true).canUnlockUntil(true).build();
		userGroup.setName((String)obj);

		UserGroup newObj = userGroupService.createUserGroup(userGroup);
		UserGroup grabbed = userGroupService.getUserGroup(newObj.getId());
	}

	private void insertAndGrabKeyRelationshipObject(Object obj){
		KeyRelationship kr = KeyRelationship.builder().repeatInterval(1).repeatType(2).expirationDateUses(true).build();

		keyRelationshipService.createKeyRelationship(kr);

	}

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
