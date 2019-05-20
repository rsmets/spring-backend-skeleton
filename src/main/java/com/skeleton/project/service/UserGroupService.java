package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.skeleton.project.dto.entity.*;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.DeleteOptions;
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

        // Need to handle grabbing nested attribute objects due to not being able to handle the list of schedule parse pointers (or really just choosing not to)
        List<Schedule> schedulesInflated = new ArrayList<>();
        List<Schedule> schedules = userGroup.getSchedule();
        // TODO figure out a batch grab
        for(Schedule schedule : schedules) {
            Schedule schedulePopulated = _scheduleService.getSchedule(schedule.getId());
            schedulesInflated.add(schedulePopulated);
        }
        userGroup.setSchedule(schedulesInflated);

        // create the key relationship mapping
        Map<String, List<KeyRelationship>> keyRelationshipMap = new HashMap<>();
        for (KeyRelationship kr : userGroup.getKeyRelationships()) {
            String userId = kr.getUser().getId();
            List<KeyRelationship> krs = keyRelationshipMap.get(userId);

            if (krs == null)
                krs = new ArrayList<>();

            krs.add(kr);
            keyRelationshipMap.put(userId, krs);
        }
        userGroup.setKeyRelationshipsMap(keyRelationshipMap);

        return saveUserGroupWithMorphia(userGroup);
    }

    @Override
    public WriteResult deleteUserGroup(String objectId) {
        WriteResult result = _database.getDatastore().delete(UserGroup.class, new ObjectId(objectId));

        return result;
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
    public List<UserGroup> getUserGroupsForUser(final String userId, final boolean administrativeAccessOnly) {
        final Query<UserGroup> query = _database.getDatastore().createQuery(UserGroup.class);

        if (administrativeAccessOnly) {
            query.or (
                    query.criteria("owner._id").equal(userId),
                    query.criteria("admins._id").equal(userId)
            );
        } else {
            query.or (
                    query.criteria("owner._id").equal(userId),
                    query.criteria("admins._id").equal(userId),
                    query.criteria("users._id").equal(userId)
            );
        }

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
        final UserGroup ug = _database.getDatastore().get(UserGroup.class, new ObjectId(objectId));

        log.info("Got user group with id " + objectId + ": " + ug);

        return ug;
    }

    /**
     * Can used for: adding users and / or locks and key relationships to group
     * TODO I think should move to coreEngine (and the reductive method)... this is higher level than grabs and deletes
     *
     * @param userGroup - with settings wanted to persist
     * @param users
     * @param keyRelationships
     * @return
     */
    @Override
    public UserGroup additiveGroupModification(UserGroup userGroup, final List<User> users, final Set<KeyRelationship> keyRelationships,
                                               final List<String> lockIds, Map<String, List<KeyRelationship>> krMaps, final Set<User> admins) {
        // TODO figure out a way to do batch update (right now making 2 calls to db)... probably just want to upsert the group obj itself
        if (users != null && !users.isEmpty())
            addUsers(userGroup, users);

        if (admins != null && !admins.isEmpty())
            addAdmins(userGroup, admins);

        if (lockIds != null && !lockIds.isEmpty())
            addLocks(userGroup, lockIds);

        // keyRelationships should always be populate by nature of either new users or locks having new krs
        return addKeyRelationships(userGroup, keyRelationships, krMaps);
    }

    @Override
    public UserGroup reductiveGroupModification(UserGroup userGroup, final List<User> users, final Set<KeyRelationship> keyRelationships,
                                                final List<String> lockIds, final Map<String, List<KeyRelationship>> krMaps, final Set<User> admins) {
        // TODO figure out a way to do batch update (right now making 2 calls to db)... probably just want to upsert the group obj itself
        if (users != null && !users.isEmpty())
            removeUsers(userGroup, users);

        if (admins != null && !admins.isEmpty())
            removeAdmins(userGroup, admins);

        if (lockIds != null && !lockIds.isEmpty())
            removeLocks(userGroup, lockIds);

        // keyRelationships should always be populate by nature of either new users or locks having new krs
        return removeKeyRelationships(userGroup, keyRelationships, krMaps);
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
        return _updateUserGroup(group, "name", newName);
    }

    /**
     * @see IUserGroupService#addKeyRelationships(UserGroup, Set, Map)
     */
    @Override
    public UserGroup addKeyRelationships(final UserGroup group, final Set<KeyRelationship> keyRelationships, final Map<String, List<KeyRelationship>> krMap) {
        Set<KeyRelationship> newKRset = group.getKeyRelationships();
        newKRset.addAll(keyRelationships);

        krMap.forEach((k, v) -> {
            List<KeyRelationship> krs = group.getKeyRelationshipsMap().get(k);
            if (krs == null)
                krs = new ArrayList<>();
            krs.addAll(v);
            group.getKeyRelationshipsMap().put(k, krs);
        });

//        for (Map.Entry<String, List<KeyRelationship>> entry : krMap.entrySet()) {
//            List<KeyRelationship> krs = group.getKeyRelationshipsMap().get(entry.getKey());
//            if (krs == null)
//                krs = new ArrayList<>();
//            krs.addAll(entry.getValue());
//        }

        _updateUserGroup(group, KeyRelationship.getAttributeNamePlural(), newKRset);
        return _updateUserGroup(group, "keyRelationshipsMap", group.getKeyRelationshipsMap());
    }

    /**
     * @see IUserGroupService#removeKeyRelationships(UserGroup, Set, Map)
     */
    @Override
    public UserGroup removeKeyRelationships(UserGroup group, Set<KeyRelationship> keyRelationships, Map<String, List<KeyRelationship>> krsMap) {
        Set<KeyRelationship> newKRset = group.getKeyRelationships();
        newKRset.removeAll(keyRelationships);

        krsMap.forEach((k, v) -> {
            List<KeyRelationship> krs = group.getKeyRelationshipsMap().get(k);
            if (krs == null)
                krs = new ArrayList<>();
            krs.removeAll(keyRelationships);

            if (krs.isEmpty())
                group.getKeyRelationshipsMap().remove(k);
            else
                group.getKeyRelationshipsMap().put(k, krs);
        });
        group.setKeyRelationshipsMap(krsMap);

        _updateUserGroup(group, KeyRelationship.getAttributeNamePlural(), newKRset);
        return _updateUserGroup(group, "keyRelationshipsMap", group.getKeyRelationshipsMap());
    }

    @Override
    public UserGroup addUsers(UserGroup group, List<User> users) {
        _addUsers(group, users);
        return _updateUserGroup(group, User.getAttributeNamePlural(), group.getUsers());
    }

    @Override
    public UserGroup addAdmins(UserGroup group, Set<User> admins) {
        _addAdmins(group, admins);
        return _updateUserGroup(group, "admins", group.getAdmins());
    }

    @Override
    public UserGroup removeUsers(UserGroup group, List<User> users) {
        _removeUsers(group, users);

        return _updateUserGroup(group, User.getAttributeNamePlural(), group.getUsers());
    }

    @Override
    public UserGroup removeAdmins(UserGroup group, Set<User> admins) {
        _removeAdmins(group, admins);

        return _updateUserGroup(group, "admins", group.getAdmins());
    }

    @Override
    public UserGroup addLocks(UserGroup group, final List<String> lockIds) {
        _addLocks(group, lockIds);
        return _updateUserGroup(group, UserGroup.getLocksIdsAttrbibuteName(), group.getLockIds());
    }

    @Override
    public UserGroup removeLocks(UserGroup group, final List<String> lockIds) {
        _removeLocks(group, lockIds);

        return _updateUserGroup(group, "lockIds", group.getLockIds());
    }

    /**
     * Helper to handle updating a single UserGroup attribute
     * @param group
     * @param attributeName
     * @param newValue
     * @return
     */
    private UserGroup _updateUserGroup(UserGroup group, String attributeName, Object newValue) {
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

    private void _addLocks(UserGroup group, final List<String> lockIds) {
        group.getLockIds().addAll(lockIds);
    }

    private void _addUsers(UserGroup group, List<User> users) {
        group.getUsers().addAll(users);
    }

    private void _addAdmins(UserGroup group, Set<User> admins) {
        group.getAdmins().addAll(admins);
    }

    private void _removeUsers(UserGroup group, final List<User> users) {
        group.getUsers().removeAll(users);
    }

    private void _removeAdmins(UserGroup group, final Set<User> admins) {
        group.getAdmins().removeAll(admins);
    }

    private void _removeLocks(UserGroup group, final List<String> lockIds) {
        group.getLockIds().removeAll(lockIds);
    }
}
