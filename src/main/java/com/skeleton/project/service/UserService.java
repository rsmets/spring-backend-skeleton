package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.domain.User;
import com.skeleton.project.dto.KeyRelationship;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
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
        return getUserWithMongoJack(objectId);
    }

    private User getUserWithMongoJack(String objectId) {
        DBCollection userCollection = _database.getDB().getCollection("_User");
        JacksonDBCollection<com.skeleton.project.dto.User, String> collection = JacksonDBCollection.wrap(userCollection, com.skeleton.project.dto.User.class, String.class);
        com.skeleton.project.dto.User user = collection.findOneById(objectId);

        log.info("key relationship from jacksonified db: " + user);

        return com.skeleton.project.domain.User.convertFromDto(user);
    }

    private User getUserWithMorpha(String objectId) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);

        final List<User> users = query
                .field("_id").equalIgnoreCase(objectId)
                .asList(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + users);

        return users.get(0); //rjs this should always be one entry
    }
}
