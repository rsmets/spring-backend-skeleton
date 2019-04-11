package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeyRelationshipService implements IKeyRelationshipService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public Key createKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        Key key = _database.getDatastore().save(keyRelationship);

        return key;
    }

    @Override
    public com.skeleton.project.domain.KeyRelationship modifyKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        return null;
    }

    @Override
    public com.skeleton.project.domain.KeyRelationship getKeyRelationshp(String objectId) {
//        return getKeyRelationshipWithMongoJack(objectId);
        return getWithParse(objectId);
    }

    @Override
    public Boolean deleteKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        return null;
    }

    private com.skeleton.project.domain.KeyRelationship getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("KeyRelationship");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    if (result != null)
                        log.info("doc from parseified db: " + result.toString());
                    else
                        log.warn("no dice getting any results");
                } else {
                    // something went wrong
                    log.error("Something went wrong", e);
                }
            }
        });

        return null;
    }

    private com.skeleton.project.domain.KeyRelationship getKeyRelationshipWithMongoJack(String objectId){
        DBCollection krCollection = _database.getDB().getCollection("KeyRelationshipService");
        JacksonDBCollection<com.skeleton.project.dto.KeyRelationship, String> collection = JacksonDBCollection.wrap(krCollection, com.skeleton.project.dto.KeyRelationship.class, String.class);
        com.skeleton.project.dto.KeyRelationship kr = collection.findOneById(objectId);

        log.info("key relationship from jacksonified db: " + kr);

        return com.skeleton.project.domain.KeyRelationship.convertFromDto(kr);
    }
}
