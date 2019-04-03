package com.skeleton.project.service;

import com.skeleton.project.domain.User;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public User createUser(String email, String username, String lastName, String firstName, String phone, int type) {
        final User user = User.builder()
                .primaryEmail(email)
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .primaryPhone(phone)
                .type(type)
                .build();

        _database.getDatastore().save(user);

        return user;
    }

    @Override
    public User getUser(String objectId) {
        final Query<User> query = _database.getDatastore().createQuery(User.class);

        final List<User> users = query
                .field("_id").equalIgnoreCase(objectId)
                .asList(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + users);

        return users.get(0); //rjs this should always be one entry
    }

}
