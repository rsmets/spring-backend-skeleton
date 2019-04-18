package com.skeleton.project.service;

import com.skeleton.project.dto.Schedule;
import com.skeleton.project.dto.UserGroup;

import java.util.List;

public interface IUserGroupService {

    /**
     * Creates a new user group with inflated Schedule and User attributes
     * @return
     */
    UserGroup createUserGroup(com.skeleton.project.dto.UserGroup userGroup);
    /**
     * Gets existing user group
     * @param objectId
     * @return
     */
    com.skeleton.project.dto.UserGroup getUserGroup(String objectId);

//    /**
//     * Gets existing user group
//     * @param objectId
//     * @return
//     */
//    UserGroup getUserGroup(Long objectId);

    /**
     * Modified existing user group
     * @param userGroup - with settings wanted to persist
     * @return
     */
    UserGroup modifyUserGroup(UserGroup userGroup);
}
