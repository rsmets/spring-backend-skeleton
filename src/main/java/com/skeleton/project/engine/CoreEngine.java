package com.skeleton.project.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.User;
import com.skeleton.project.facade.rest.IClient;
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

	@Override
	public BaseResponse executeAction(Object example) {

		try{
			BaseResponse response = _client.doAction(example);

			// do something... like find the pot of gold at end of the rainbow
			getDbObject(example);

			return response;

		} catch (Exception e) {
			// probably want to do something clever here.
			log.error("That request did not work... ", e);

		}

		return null;

	}

	private Object getDbObject(Object search) {
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

		return userCollection;
	}
}
