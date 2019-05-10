package com.skeleton.project.service;

import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Lock;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.UserGroup;

import java.util.List;

public interface IUserGroupService {

    /**
     * Creates a new user group with inflated Schedule and User attributes
     * @return
     */
    UserGroup createUserGroup(UserGroup userGroup);

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
    List<UserGroup> getUserGroupsForUser(String userId);

    /**
     * Additive modification to existing user group. Validation is performed in core engine
     *
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup additiveGroupModification(UserGroup userGroup, List<User> users, List<KeyRelationship> keyRelationships, List<String> lockIds);

    /**
     * Reducing modification to existing user group. Validation is performed in core engine
     *
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup reductiveGroupModification(UserGroup userGroup, List<User> users, List<KeyRelationship> keyRelationships, List<String> lockIds);


    /**
     * Modify group name
     * @param groupId
     * @param newName
     * @return
     */
    UserGroup modifyGroupName(UserGroup groupId, String newName);

    /**
     * Add users to the specified group.
     * Validation is done in the core engine
     */
    UserGroup addUsers(String id, List<User> user);
    UserGroup addUsers(UserGroup group, List<User> users);

    /**
     * Removes users from the specified group.
     * Validation is done in the core engine
     */
    UserGroup removeUsers(UserGroup group, List<User> users);

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
    UserGroup addKeyRelationships(UserGroup group, List<KeyRelationship> keyRelationships);

    /**
     * Removes key relationships from group
     */
    UserGroup removeKeyRelationships(UserGroup group, List<KeyRelationship> keyRelationships);
}
