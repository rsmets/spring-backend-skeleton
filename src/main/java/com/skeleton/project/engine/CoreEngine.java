package com.skeleton.project.engine;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.facade.rest.IClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Arrays;

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

		return userCollection;
	}
}
