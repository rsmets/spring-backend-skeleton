package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @see IUserGroupService#createUserGroup(UserGroup)
     */
    @Override
    public UserGroup createUserGroup(UserGroup userGroup) {

        // Need to handle grabbing nested attribute objects
        List<Schedule> schedulesInflated = new ArrayList<>();
        List<Schedule> schedules = userGroup.getSchedule();
        for(Schedule schedule : schedules) {
            Schedule schedulePopulated = _scheduleService.getSchedule(schedule.getId());
            schedulesInflated.add(schedulePopulated);
        }
        userGroup.setSchedule(schedulesInflated);

        List<User> usersInflated = new ArrayList<>();
        List<User> users = userGroup.getUsers();
//        Set<User> usersInflated = new HashSet<>();
//        Set<User> users = userGroup.getUsers();
        for(User user : users) {
            User userPopulated = user.getId() != null ? _userService.getUser(user.getId()) : _userService.getUserByPhone(user.getPrimaryPhone());
            usersInflated.add(userPopulated);
        }
        userGroup.setUsers(usersInflated);

        /**
         * RJS 4/16/19 Do I really really need to inflate? Opting not to for keyRelationship.
         * I think that having the objectIds should be enough...
         */

        return saveUserGroupWithMorphia(userGroup);
    }

    private UserGroup saveUserGroupWithMorphia(UserGroup userGroup) {
        // Populates the id field
        _database.getDatastore().save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup getUserGroup(String objectId) {
//        return getWithParse(objectId);
//        return getUserGroupWithMongoJack(objectId);
        return getUserGroupWithMorphia(objectId);
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
        JacksonDBCollection<UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, UserGroup.class, String.class);
        UserGroup ug = collection.findOneById(objectId);

        log.info("user group from jacksonified db: " + ug);

        return ug;
    }

    private UserGroup getUserGroupWithMorphia(String objectId){
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final UserGroup userGroups = query
                .disableValidation()
                .field("_id").equalIgnoreCase(new ObjectId(objectId))
                .get(); //todo figure out how to query for one.

        final UserGroup ug = _database.getDatastore().get(UserGroup.class, new ObjectId(objectId));

        log.info("Got user group with id " + objectId + ": " + ug);

//        return userGroups.get(0); //rjs this should always be one entry
//        return userGroups;
//        return UserGroup.convertFromDto(ug);
        return ug;
    }

    @Override
    public UserGroup modifyUserGroup(UserGroup userGroup) {
        return null;
    }

    /**
     * @see IUserGroupService#addUsers(String, List)
     */
    @Override
    public UserGroup addUsers(String id, List<User> users) {
        UserGroup group = getUserGroup(id);

        group.getUsers().addAll(users);

        UserGroup result = saveUserGroupWithMorphia(group);
        return result;
    }

    @Override
    public UserGroup addUsers(UserGroup group, List<User> users) {
        group.getUsers().addAll(users);

        saveUserGroupWithMorphia(group);
        return group;
    }
}
