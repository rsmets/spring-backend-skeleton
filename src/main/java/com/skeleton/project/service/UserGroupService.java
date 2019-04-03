package com.skeleton.project.service;

import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.User;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
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
    public UserGroup getUserGroup(String objectId) {
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);

        final List<UserGroup> userGroups = query
                .field("_id").equalIgnoreCase(objectId)
                .asList(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + userGroups);

        return userGroups.get(0); //rjs this should always be one entry
    }

    @Override
    public UserGroup modifyUserGroup(UserGroup userGroup) {
        return null;
    }
}
