package com.skeleton.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.skeleton.project.core.DatabaseDriver;
import com.skeleton.project.dto.KeyRelationship;
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
    public Key createKeyRelationship(KeyRelationship keyRelationship) {
//        return createKRwithMorphia(keyRelationship);
        return createKeyRelationshipWith4j(keyRelationship);
    }

    private Key createKeyRelationshipWith4j(KeyRelationship keyRelationship) {
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

    private Key createKRwithMorphia(KeyRelationship keyRelationship) {
        Key key = _database.getDatastore().save(keyRelationship);

        return key;
    }

    @Override
    public KeyRelationship modifyKeyRelationship(KeyRelationship keyRelationship) {
        return null;
    }

    @Override
    public KeyRelationship getKeyRelationship(String objectId) {
//        return getKeyRelationshipWithMongoJack(objectId);
//        return getWithParse(objectId);
        return getKeyRWithMorphia(objectId);
    }

    @Override
    public List<KeyRelationship> getKeyRelationshipsByUser(String userObjectId) {
        final Query<com.skeleton.project.dto.KeyRelationship> query = _database.getDatastore().createQuery(com.skeleton.project.dto.KeyRelationship.class);

        final String pointerString = "_User$" + userObjectId;
        final List<com.skeleton.project.dto.KeyRelationship> krs = query
                .disableValidation()
                .filter("_p_user", pointerString)
                .asList();

        log.info("Got key relationships with user " + userObjectId + ": " + krs);

        if (krs.isEmpty())
            return null;

        return krs;
    }

    @Override
    public KeyRelationship getKeyRelationship(String userObjectId, String lockObjectId) {
        final Query<com.skeleton.project.dto.KeyRelationship> query = _database.getDatastore().createQuery(com.skeleton.project.dto.KeyRelationship.class);

        final String userPointerString = "_User$" + userObjectId;
        final String keyPointerString = "Lock$" + lockObjectId;

        final KeyRelationship kr = query
                .disableValidation()
                .filter("_p_user", userPointerString)
                .filter("_p_key", keyPointerString)
                .get();

        log.info("Got key relationship with user " + userObjectId + " and lockObject : " + lockObjectId + ": " + kr);

        return kr;
    }

    @Override
    public Boolean deleteKeyRelationship(KeyRelationship keyRelationship) {

        return null;
    }

    private KeyRelationship getWithParse(String objectId) {
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
            return krDto;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyRelationship getKeyRWithMorphia(String objectId) {
        final Query<KeyRelationship> query = _database.getDatastore().createQuery(KeyRelationship.class).disableValidation();

        final KeyRelationship kr = query
                .field("_id").equal(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got kr with id " + objectId + ": " + kr);

        return kr;
    }
}
