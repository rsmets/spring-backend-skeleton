package com.skeleton.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.skeleton.project.dto.KeyRelationship;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class KeyRelationshipService implements IKeyRelationshipService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public Key createKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {
//        return createKRwithMorphia(keyRelationship);
        return createKeyRelationshipWith4j(keyRelationship);
    }

    private Key createKeyRelationshipWith4j(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        ParseObject kr = new ParseObject("KeyRelationship");

        kr.put("repeatType", keyRelationship.getRepeatType());
        kr.put("expirationDateUsesNumOccurrences", keyRelationship.isExpirationDateUses());
        kr.put("repeatInterval", keyRelationship.getRepeatInterval());

        try {
            kr.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Key createKRwithMorphia(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        Key key = _database.getDatastore().save(keyRelationship);

        return key;
    }

    @Override
    public com.skeleton.project.domain.KeyRelationship modifyKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {
        return null;
    }

    @Override
    public com.skeleton.project.domain.KeyRelationship getKeyRelationship(String objectId) {
//        return getKeyRelationshipWithMongoJack(objectId);
//        return getWithParse(objectId);
        return getKeyRWithMorphia(objectId);
    }

    @Override
    public List<com.skeleton.project.domain.KeyRelationship> getKeyRelationshipsByUser(String userObjectId) {
        final Query<com.skeleton.project.dto.KeyRelationship> query = _database.getDatastore().createQuery(com.skeleton.project.dto.KeyRelationship.class);

        final String pointerString = "_User$" + userObjectId;
        final List<com.skeleton.project.dto.KeyRelationship> krs = query
                .disableValidation()
                .filter("_p_user", pointerString)
                .asList();

        log.info("Got key relationships with user " + userObjectId + ": " + krs);

        if (krs.isEmpty())
            return null;

        List<com.skeleton.project.domain.KeyRelationship> result = com.skeleton.project.domain.KeyRelationship.convertFromDtos(krs);
        return result;
    }

    @Override
    public com.skeleton.project.domain.KeyRelationship getKeyRelationship(String userObjectId, String lockObjectId) {
        final Query<com.skeleton.project.dto.KeyRelationship> query = _database.getDatastore().createQuery(com.skeleton.project.dto.KeyRelationship.class);

        final String userPointerString = "_User$" + userObjectId;
        final String keyPointerString = "Lock$" + lockObjectId;

        final List<com.skeleton.project.dto.KeyRelationship> krs = query
                .disableValidation()
                .filter("_p_user", userPointerString)
                .filter("_p_key", keyPointerString)
                .asList();

        log.info("Got key relationship with user " + userObjectId + " and lockObject : " + lockObjectId + ": " + krs);

        if (krs.isEmpty())
            return null;
        else if (krs.size() > 1)
            log.error("This should only ever return one, instead got multiple!");

        com.skeleton.project.domain.KeyRelationship result = com.skeleton.project.domain.KeyRelationship.convertFromDto(krs.get(0)); //should only ever be one
        return result;
    }

    @Override
    public Boolean deleteKeyRelationship(com.skeleton.project.domain.KeyRelationship keyRelationship) {

        return null;
    }

    private com.skeleton.project.domain.KeyRelationship getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("KeyRelationship");



//        query.getInBackground(objectId, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject result, ParseException e) {
//                if (e == null) {
//                    if (result != null)
//                        log.info("doc from parseified db: " + result.toString());
//                    else
//                        log.warn("no dice getting any results");
//                } else {
//                    // something went wrong
//                    log.error("Something went wrong", e);
//                }
//            }
//        });

        try {
            ParseObject result = query.get(objectId);

            if (result != null)
                log.info("doc from parseified db: " + result.toString());
            else
                log.warn("no dice getting any results");

            log.info(result.toString());
            log.info(result.getParseData().toString());

            KeyRelationship krDto = new ObjectMapper().readValue(result.getParseData().toString(), KeyRelationship.class);
            com.skeleton.project.domain.KeyRelationship kr = com.skeleton.project.domain.KeyRelationship.convertFromDto(krDto);
            return kr;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private com.skeleton.project.domain.KeyRelationship getKeyRWithMorphia(String objectId) {
        final Query<KeyRelationship> query = _database.getDatastore().createQuery(KeyRelationship.class).disableValidation();
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final KeyRelationship kr = query
                .field("_id").equal(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got kr with id " + objectId + ": " + kr);

//        return userGroups.get(0); //rjs this should always be one entry
        return com.skeleton.project.domain.KeyRelationship.convertFromDto(kr);
    }

    private com.skeleton.project.domain.KeyRelationship getKeyRelationshipWithMongoJack(String objectId){
        DBCollection krCollection = _database.getDB().getCollection("KeyRelationshipService");
        JacksonDBCollection<com.skeleton.project.dto.KeyRelationship, String> collection = JacksonDBCollection.wrap(krCollection, com.skeleton.project.dto.KeyRelationship.class, String.class);
        com.skeleton.project.dto.KeyRelationship kr = collection.findOneById(objectId);
//        DBCursor<KeyRelationship> cursor = collection.find(DBQuery.is("_id", objectId));
//
//        if(cursor.hasNext()) {
//            com.skeleton.project.dto.KeyRelationship kr = cursor.next();
//            log.info("key relationship from jacksonified db: " + kr);
//
//            return com.skeleton.project.domain.KeyRelationship.convertFromDto(kr);
//        }

        log.info("key relationship from jacksonified db: " + kr);

        return com.skeleton.project.domain.KeyRelationship.convertFromDto(kr);
//        return null;
    }
}
