package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.User;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserGroupService implements IUserGroupService {

    @Autowired
    DatabaseDriver _database;

    @Autowired
    IUserService _userService;

    @Override
    public UserGroup createUserGroup(List<String> adminIds, List<String> lockIds, Schedule schedule, List<String> userIds, boolean canUsersRemoteUnlock, boolean canUsersUnlockUntil) throws Exception {
        List<User> admins = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // can do these loops in an separate executor thread...

        for (String id : adminIds) {
            User user = _userService.getUser(id);
            if (user == null) {
                // need to throw an error... the users should all exist. (for now)
                throw new Exception("User with id " + id + " should exist");
            }
            admins.add(user);
        }

        for (String id : userIds) {
            User user = _userService.getUser(id);
            if (user == null) {
                // need to throw an error... the users should all exist. (for now)
                throw new Exception("User with id " + id + " should exist");
            }
            users.add(user);
        }


        final UserGroup userGroup = UserGroup.builder()
                .admins(admins)
                .users(users)
                .canRemoteUnlock(canUsersRemoteUnlock)
                .canUnlockUntil(canUsersUnlockUntil)
                .schedule(schedule)
                .build();

        _database.getDatastore().save(userGroup);

        return userGroup;
    }

    @Override
    public String createUserGroup(UserGroup userGroup) {
        return createUserGroupWithMongoJack(userGroup);
//        return createUserGroupWithMorphia(userGroup);
    }

    private String createUserGroupWithMongoJack(UserGroup userGroup) {
        // TODO if do decide to stick with mongojack can put this collectioZn def in constructor
        DBCollection userGroupCollection = _database.getDB().getCollection("UserGroup");
        JacksonDBCollection<UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, UserGroup.class, String.class);

        WriteResult writeResult = collection.insert(userGroup);

        String newObjId = (String)writeResult.getSavedId();
        return newObjId;
    }

    private String createUserGroupWithMorphia(UserGroup userGroup) {
        Key key = _database.getDatastore().save(userGroup);

        return (String)key.getId();
    }

    @Override
    public UserGroup getUserGroup(String objectId) {
        return getUserGroupWithMongoJack(objectId);
//        return getUserGroupWithMorphia(objectId);
    }

    private UserGroup getUserGroupWithMongoJack(String objectId){
        DBCollection userGroupCollection = _database.getDB().getCollection("UserGroup");
        JacksonDBCollection<com.skeleton.project.dto.UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, com.skeleton.project.dto.UserGroup.class, String.class);
        com.skeleton.project.dto.UserGroup ug = collection.findOneById(objectId);

        log.info("user group from jacksonified db: " + ug);

        return UserGroup.convertFromDto(ug);
    }

    private UserGroup getUserGroupWithMorphia(String objectId){
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final UserGroup userGroups = query
                .field("_id").equal(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + userGroups);

//        return userGroups.get(0); //rjs this should always be one entry
        return userGroups;
    }

    @Override
    public UserGroup modifyUserGroup(UserGroup userGroup) {
        return null;
    }
}
