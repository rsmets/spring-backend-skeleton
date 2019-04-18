package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.dto.Schedule;
import com.skeleton.project.dto.User;
import com.skeleton.project.dto.UserGroup;
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
     * @see IUserGroupService#createUserGroup(UserGroup)
     */
    @Override
    public com.skeleton.project.dto.UserGroup createUserGroup(com.skeleton.project.dto.UserGroup userGroup) {

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
        for(User user : users) {
            User userPopulated = user.getId() != null ? _userService.getUser(user.getId()) : _userService.getUserByPhone(user.getPrimaryPhone());
            usersInflated.add(userPopulated);
        }
        userGroup.setUsers(usersInflated);

        /**
         * RJS 4/16/19 Do I really really need to inflate? Opting not to for keyRelationship.
         * I think that having the objectIds should be enough...
         */

        return createUserGroupWithMorphia(userGroup);
    }

    private com.skeleton.project.dto.UserGroup createUserGroupWithMorphia(com.skeleton.project.dto.UserGroup userGroup) {
        // Populates the id field
        _database.getDatastore().save(userGroup);
        return userGroup;
    }

    @Override
    public com.skeleton.project.dto.UserGroup getUserGroup(String objectId) {
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
        JacksonDBCollection<com.skeleton.project.dto.UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, com.skeleton.project.dto.UserGroup.class, String.class);
        com.skeleton.project.dto.UserGroup ug = collection.findOneById(objectId);

        log.info("user group from jacksonified db: " + ug);

        return ug;
    }

    private com.skeleton.project.dto.UserGroup getUserGroupWithMorphia(String objectId){
        final Query<com.skeleton.project.dto.UserGroup> query = _database.getDatastore().createQuery(com.skeleton.project.dto.UserGroup.class);
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final com.skeleton.project.dto.UserGroup userGroups = query
                .disableValidation()
                .field("_id").equalIgnoreCase(new ObjectId(objectId))
                .get(); //todo figure out how to query for one.

        final com.skeleton.project.dto.UserGroup ug = _database.getDatastore().get(com.skeleton.project.dto.UserGroup.class, new ObjectId(objectId));

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
}
