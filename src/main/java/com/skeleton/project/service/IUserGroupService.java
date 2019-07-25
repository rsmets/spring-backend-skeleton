package com.skeleton.project.service;

import com.mongodb.WriteResult;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserGroupService {

    /**
     * Creates a new user group with inflated Schedule and User attributes
     * @return
     */
    UserGroup createUserGroup(UserGroup userGroup);

    /**
     * Deletes a user group by objectId
     * @param objectId
     * @return
     */
    WriteResult deleteUserGroup(String objectId);

    /**
     * Gets existing user group
     * @param objectId
     * @return
     */
    UserGroup getUserGroup(String objectId);

    /**
     * Gets a list of user groups where the user is an owner or an admin of
     * @param userId
     * @return
     */
    List<UserGroup> getUserGroupsForUser(String userId, boolean administrativeAccessOnly, boolean ownerAccessOnly);

    /**
     * Additive modification to existing user group. Validation is performed in core engine
     *
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup additiveGroupModification(UserGroup userGroup, List<User> users, Set<KeyRelationship> keyRelationships, List<String> lockIds, Map<String, List<KeyRelationship>> krMaps, Set<User> admins);

    /**
     * Reducing modification to existing user group. Validation is performed in core engine
     *
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup reductiveGroupModification(UserGroup userGroup, List<User> users, Set<KeyRelationship> keyRelationships, List<String> lockIds, Set<User> admins);


    /**
     * Modify group name
     * @param group
     * @param newName
     * @return
     */
    UserGroup modifyGroupName(UserGroup group, String newName);

    /**
     * Modify group name
     * @param group
     * @param newDescription
     * @return
     */
    UserGroup modifyGroupDescription(UserGroup group, String newDescription);

    /**
     * Modify group name
     * @param group
     * @param remoteUnlock, unlockUntil
     * @return
     */
    UserGroup modifyGroupSpecialPower(UserGroup group, Boolean remoteUnlock, Boolean unlockUntil);

    /**
     * Modify group name
     * @param group
     * @param newName
     * @return
     */
    UserGroup modifyGroupDetails(UserGroup group, String newName, String newDescription, Boolean remoteUnlock, Boolean unlockUntil);

    /**
     * Modify group schedule
     * @param group
     * @param schedule
     * @return
     */
    UserGroup modifyGroupSchedule(UserGroup group, List<Schedule> schedule);

    /**
     * Add users to the specified group.
     * Validation is done in the core engine
     */
    UserGroup addUsers(String id, List<User> user);
    UserGroup addUsers(UserGroup group, List<User> users);

    /**
     * Add admins to the specified group.
     * Validation is done in the core engine
     */
    UserGroup addAdmins(UserGroup group, Set<User> admin);

    /**
     * Removes users from the specified group.
     * Validation is done in the core engine
     */
    UserGroup removeUsers(UserGroup group, List<User> users);

    /**
     * Removes admins from the specified group.
     * Validation is done in the core engine
     */
    UserGroup removeAdmins(UserGroup group, Set<User> admins);

    /**
     * Add locks to the specified group.
     * Validation is done in the core engine.
     */
    UserGroup addLocks(UserGroup group, List<String> lockIds);

    /**
     * Removes locks from the specified group.
     * Validation is done in the core engine.
     */
    UserGroup removeLocks(UserGroup group, List<String> lockIds);

    /**
     * Adds key relationships to group
     */
    UserGroup addKeyRelationships(UserGroup group, Set<KeyRelationship> keyRelationships, Map<String, List<KeyRelationship>> krMap);

    /**
     * Removes key relationships from group
     */
    UserGroup removeKeyRelationships(UserGroup group, Set<KeyRelationship> keyRelationships);
}
