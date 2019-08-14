package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.core.DatabaseDriver;
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
    public Key createUser(User user) {
        Key key = _database.getDatastore().save(user);

        return key;
    }

    @Override
    public User getUser(String objectId) {
        return getWithMorphia(objectId);
//        return getUserWithMongoJack(objectId);
//        return getWithParse(objectId);
    }

    @Override
    public User getUserByPhone(String phoneNumber) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);

        final List<User> users = query
                .disableValidation()
                .filter("primaryPhone", phoneNumber)
                .asList(); //todo figure out how to query for one.

        log.info("Got users with phoneNumber " + phoneNumber + ": " + users);

        if (users.isEmpty())
            return null;
        else if (users.size() > 1)
            log.error("Something is wrong query for users by primary phone number " + phoneNumber + "returned more than one. Selecting the first for now.");

        return users.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);

        final User user = query
                .disableValidation()
                .filter("primaryEmail", email)
                .get(); //todo figure out how to query for one.

        log.info("Got user with email " + email + ": " + user);

        return user;
    }

    private User getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
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

    private User getWithMorphia(String objectId) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);


        final User user = query
                .disableValidation()
                .field("_id").equalIgnoreCase(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + user);

        return user;
    }
}
