package com.skeleton.project.engine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.dto.User;
import com.skeleton.project.facade.rest.IClient;
import com.skeleton.project.jackson.UserDeserializer;
import com.skeleton.project.service.IUserGroupService;
import com.skeleton.project.service.IUserService;
import dev.morphia.Key;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CoreEngine implements ICoreEngine{

	@Autowired
	IClient _client;

	@Autowired
	DatabaseDriver database;

	@Autowired
	IUserGroupService userGroupService;

	@Override
	public BaseResponse executeAction(Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			User user = (User) getDbObject(example);
			com.skeleton.project.domain.User fullUser = (com.skeleton.project.domain.User) getDbFullObject(example);

			insertDbObject(null);

			return BaseResponse.builder().example(user).build();

		} catch (Exception e) {
			log.error("That request did not work... ", e);
		}

		return null;

	}

	private void insertDbObject(Object obj){
//		MongoCollection<Document>  userCollection = database.getDatabase().getCollection("_User");
//
//		com.skeleton.project.domain.User user = (com.skeleton.project.domain.User) getDbFullObject(null);
//
////		log.info("user full obj to string " + user.toString());
////		DBObject dbObject = (DBObject) JSON.parse(user.toString());
//
//		Document document = new Document("primaryPhone", "+1111");

//		MongoCollection<Document>  userCollection = database.getDatabase().getCollection("UserGroup");
		DBCollection userCollection = database.getDB().getCollection("UserGroup");
		Document document = new Document("name", "+222");

		ObjectMapper oMapper = new ObjectMapper();
		UserGroup userGroup = UserGroup.builder().canRemoteUnlock(true).canUnlockUntil(true).build();
		userGroup.setName("first");

		Map<String, Object> map = oMapper.convertValue(userGroup, Map.class);
//		DBObject dbObject = (DBObject) JSON.parse(map.toString());

//		userCollection.insertOne(document);
		JacksonDBCollection<UserGroup, String> collection = JacksonDBCollection.wrap(userCollection, UserGroup.class, String.class);
		WriteResult writeResult = collection.insert(userGroup);
		String newObjId = (String)writeResult.getSavedId();

		UserGroup grabbed = userGroupService.getUserGroup(newObjId);

		userGroup.setCanUnlockUntil(false);
		userGroup.setCanRemoteUnlock(false);
		userGroup.setName("haha");

//		String key = userGroupService.createUserGroup(userGroup);
//		UserGroup grabbed = userGroupService.getUserGroup(key);
	}

	private User getDbObject(Object search) {
		MongoCollection<Document>  userCollection = database.getDatabase().getCollection("_User");

		Document doc = userCollection.find().first();
		log.info("doc from db: " + doc.toJson());

		ObjectMapper mapper = new ObjectMapper();

		User user = null;
		try {
			user = mapper.readValue(doc.toJson(), User.class);
			log.info("doc from jacksonified db: " + user.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return user;
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
