package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateResults;
import lombok.extern.slf4j.Slf4j;
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
    public UserGroup getUserGroup(final String objectId) {
        return getUserGroupWithMorphia(objectId);
    }

    @Override
    public List<UserGroup> getUserGroupsForUser(final String userId) {
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);
        query.or (
                query.criteria("owner._id").equal(userId),
                query.criteria("admins._id").equal(userId)
                );

        final List<UserGroup> groups = query.disableValidation().asList();

        log.info("Got user groups for user " + userId + ": " + groups);

        return groups;
    }

    private UserGroup getWithParse(final String objectId) {
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

    private UserGroup getUserGroupWithMongoJack(final String objectId){
        DBCollection userGroupCollection = _database.getDB().getCollection("UserGroup");
        JacksonDBCollection<UserGroup, String> collection = JacksonDBCollection.wrap(userGroupCollection, UserGroup.class, String.class);
        UserGroup ug = collection.findOneById(objectId);

        log.info("user group from jacksonified db: " + ug);

        return ug;
    }

    private UserGroup getUserGroupWithMorphia(final String objectId) {
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);
//        final UserGroup res = _database.getDatastore().getByKey(UserGroup.class, objectId);

        final UserGroup userGroups = query
                .disableValidation()
                .field("_id").equalIgnoreCase(new ObjectId(objectId))
                .get(); //todo figure out how to query for one.

        final UserGroup ug = _database.getDatastore().get(UserGroup.class, new ObjectId(objectId));

        log.info("Got user group with id " + objectId + ": " + ug);

        return ug;
    }

    /**
     * Can used for: adding users to group, ...
     * @param userGroup - with settings wanted to persist
     * @param users
     * @param keyRelationships
     * @return
     */
    @Override
    public UserGroup modifyUserGroup(UserGroup userGroup, final List<User> users, final List<KeyRelationship> keyRelationships) {

        userGroup = addKeyRelationships(userGroup, keyRelationships);
        return addUsers(userGroup, users);
    }

    /**
     * @see IUserGroupService#addUsers(String, List)
     */
    @Override
    public UserGroup addUsers(final String id, final List<User> users) {
        UserGroup group = getUserGroup(id);

        return addUsers(group, users);
    }

    /**
     * @see IUserGroupService#modifyGroupName(UserGroup, String)
     */
    @Override
    public UserGroup modifyGroupName(final UserGroup group, final String newName) {
        group.setName(newName); // TODO figure out the proper update and fetch in one go. Till then this is a little hack to return the seemingly new db group obj
        return updateUserGroup(group, "name", newName);
    }

    /**
     * @see IUserGroupService#addKeyRelationships(UserGroup, List)
     */
    @Override
    public UserGroup addKeyRelationships(final UserGroup group, final List<KeyRelationship> keyRelationships) {
        Set<KeyRelationship> newKRset = group.getKeyRelationships();
        newKRset.addAll(keyRelationships);

        return updateUserGroup(group, KeyRelationship.getAttributeNamePlural(), newKRset);
    }

    /**
     * Helper to handle updating a single UserGroup attribute
     * @param group
     * @param attributeName
     * @param newValue
     * @return
     */
    private UserGroup updateUserGroup(UserGroup group, String attributeName, Object newValue) {
        /**
         * Note: Need to use the Morphia filter method instead of find due as the difference are one does a regex mongo $regex lookup (that is find) and the other
         * does a $oid lookup (filter). When not using a bjson ObjectId on the entity the find seems to fail for update queries (but works for regular finds?)
         */
        Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class).disableValidation().filter("_id", new ObjectId(group.getId()));
        UpdateOperations<UserGroup> ops =  _database.getDatastore().createUpdateOperations(UserGroup.class).set(attributeName, newValue);

        UpdateResults results = _database.getDatastore().update(query, ops);
        if (results.getWriteResult().getN() == 0) {
            log.error("Something went wrong during db UserGroup object update");
        }

        // Due to verifying an update took place can just return the pojo with the update to save a db round trip.
        // TODO figure out the update and return function (in one call / trip)...
        return group;
    }

    @Override
    public UserGroup addUsers(UserGroup group, List<User> users) {

        Set<User> newUserSet = group.getUsers();
        newUserSet.addAll(users);

        return updateUserGroup(group, User.getAttributeNamePlural(), newUserSet);
    }
}
