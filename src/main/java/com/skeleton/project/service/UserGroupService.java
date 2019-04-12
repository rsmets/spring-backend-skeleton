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
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
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

    @Autowired
    IScheduleService _scheduleService;

    /**
     * @see IUserGroupService#createUserGroup(List, List, List, List, boolean, boolean)
     */
    @Override
    public UserGroup createUserGroup(List<String> adminIds, List<String> lockIds, List<Schedule> schedule, List<String> userIds, boolean canUsersRemoteUnlock, boolean canUsersUnlockUntil) throws Exception {
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

    /**
     * @see IUserGroupService#createUserGroup(UserGroup) 
     */
    @Override
    public String createUserGroup(UserGroup userGroup) {

        // Need to handle making and saving the nested objects first
        List<Schedule> schedulesInflated = new ArrayList<>();
        List<Schedule> schedules = userGroup.getSchedule();
        for(Schedule schedule : schedules) {
            Schedule schedulePopulated = _scheduleService.getSchedule(schedule.getId());
            schedulesInflated.add(schedulePopulated);
        }

        List<User> usersInflated = new ArrayList<>();
        List<User> users = userGroup.getUsers();
        for(User user : users) {
            User userPopulated = _userService.getUser(user.getId());
            usersInflated.add(userPopulated);
        }

        userGroup.setSchedule(schedulesInflated);
        userGroup.setUsers(usersInflated);

//        return createUserGroupWithMongoJack(userGroup);
        return createUserGroupWithMorphia(userGroup);
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
//        return getWithParse(objectId);
        return getUserGroupWithMongoJack(objectId);
//        return getUserGroupWithMorphia(objectId);
    }

    private UserGroup getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGroup");
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

    private UserGroup getUserGroupWithMongoJack(String objectId){
        DBCollection userGroupCollection = _database.getDB().getCollection("UserGroup");
        JacksonDBCollection<com.skeleton.project.dto.UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, com.skeleton.project.dto.UserGroup.class, String.class);
        com.skeleton.project.dto.UserGroup ug = collection.findOneById(objectId);

        log.info("user group from jacksonified db: " + ug);

        return UserGroup.convertFromDto(ug);
    }

    private UserGroup getUserGroupWithMorphia(String objectId){
        final Query<com.skeleton.project.dto.UserGroup> query = _database.getDatastore().createQuery(com.skeleton.project.dto.UserGroup.class);
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final com.skeleton.project.dto.UserGroup userGroups = query
                .field("_id").equal(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got users with id " + objectId + ": " + userGroups);

//        return userGroups.get(0); //rjs this should always be one entry
//        return userGroups;
        return UserGroup.convertFromDto(userGroups);
    }

    @Override
    public UserGroup modifyUserGroup(UserGroup userGroup) {
        return null;
    }
}
