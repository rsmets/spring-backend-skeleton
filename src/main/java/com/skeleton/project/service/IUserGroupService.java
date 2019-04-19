package com.skeleton.project.service;

import com.skeleton.project.dto.entity.KeyRelationship;
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
     * Modified existing user group
     * TODO maybe and another param with the request user to see if have admin access to the group
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup modifyUserGroup(UserGroup userGroup, List<User> users, List<KeyRelationship> keyRelationships);

    /**
     * Add user to the specified group
     * TODO maybe and another param with the request user to see if have admin access to the group
     * @param id
     * @param user
     * @return
     */
    UserGroup addUsers(String id, List<User> user);

    UserGroup addUsers(UserGroup group, List<User> users);
}
