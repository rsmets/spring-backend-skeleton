package com.skeleton.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.DBCollection;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateResults;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        Set<User> usersInflated = new HashSet<>();
        Set<User> users = userGroup.getUsers();
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

        return addUsers(group, users);
    }

    private UserGroup modifyGroupName(String id, String newName) {
        Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class).disableValidation().filter("_id", new ObjectId(id));
        UpdateOperations<UserGroup> ops =  _database.getDatastore().createUpdateOperations(UserGroup.class).set("name", newName);

        UpdateResults results = _database.getDatastore().update(query, ops);

        return getUserGroup(id);
    }

    @Override
    public UserGroup addUsers(UserGroup group, List<User> users) {

        Set<User> newUserSet = group.getUsers();
        newUserSet.addAll(users);

        /**
         * Note: Need to use the Morphia filter method instead of find due as the difference are one does a regex mongo $regex lookup (that is find) and the other
         * does a $oid lookup (filter). When not using a bjson ObjectId on the entity the find seems to fail for update queries (but works for regular finds?)
         */
        Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class).disableValidation().filter("_id", new ObjectId(group.getId()));
        UpdateOperations<UserGroup> ops =  _database.getDatastore().createUpdateOperations(UserGroup.class).set("users", newUserSet);

        UpdateResults results = _database.getDatastore().update(query, ops);
        if (results.getWriteResult().getN() == 0) {
            log.error("Something went wrong during db " + group.getClass().getSimpleName() + " object update");
        }

        // Due to verifying an update took place can just return the pojo with the update to save a db round trip
        return group;
    }
}
