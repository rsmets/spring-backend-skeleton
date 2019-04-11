package com.skeleton.project.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.User;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.facade.rest.IClient;
import com.skeleton.project.jackson.UserDeserializer;
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

	@Override
	public BaseResponse executeAction(Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			com.skeleton.project.domain.User fullUser = (com.skeleton.project.domain.User) getDbFullObject(example);

			insertAndGrabDbObject(example);

			User user = userService.getUser("3l6FvM305C");

			return BaseResponse.builder().example(user).build();

		} catch (Exception e) {
			log.error("That request did not work... ", e);
		}

		return null;

	}

	private User getUser(String objId) {
		return userService.getUser(objId);
	}

	private void insertAndGrabDbObject(Object obj){
		UserGroup userGroup = UserGroup.builder().canRemoteUnlock(true).canUnlockUntil(true).build();
		userGroup.setName((String)obj);

		String newObjId = userGroupService.createUserGroup(userGroup);
		UserGroup grabbed = userGroupService.getUserGroup(newObjId);
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
