package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.domain.User;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public Key createUser(String email, String username, String lastName, String firstName, String phone, int type) {
        final User user = User.builder()
                .primaryEmail(email)
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .primaryPhone(phone)
                .type(type)
                .build();

        Key key = _database.getDatastore().save(user);

        return key;
    }

    @Override
    public Key createUser(User user) {
        Key key = _database.getDatastore().save(user);

        return key;
    }

    @Override
    public User getUser(String objectId) {
//        return getUserWithMorpha(objectId);
//        return getUserWithMongoJack(objectId);
        return getWithParse(objectId);
    }

    private User getUserWithMongoJack(String objectId) {
        DBCollection userCollection = _database.getDB().getCollection("_User");
        JacksonDBCollection<com.skeleton.project.dto.User, String> collection = JacksonDBCollection.wrap(userCollection, com.skeleton.project.dto.User.class, String.class);
        com.skeleton.project.dto.User user = collection.findOneById(objectId);

        log.info("key relationship from jacksonified db: " + user);

        return com.skeleton.project.domain.User.convertFromDto(user);
    }

    private User getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground("3l6FvM305C", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    log.info("doc from parseified db: " + user.toString());
                } else {
                    // something went wrong
                    log.error("Something went wrong", e);
                }
            }
        });

        return null;
    }

    private User getWithMorphia(String objectId) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);

        final List<User> users = query
                .field("_id").equalIgnoreCase(objectId)
                .asList(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + users);

        return users.get(0); //rjs this should always be one entry
    }
}
