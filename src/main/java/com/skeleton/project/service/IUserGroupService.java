package com.skeleton.project.service;

import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.UserGroup;

import java.util.List;

public interface IUserGroupService {

    /**
     * Creates a new user group with inflated Schedule and User attributes
     * @param adminIds
     * @param lockIds
     * @param schedule note: must be already saved Schedule objects
     * @param userIds note: must be already saved User objects
     * @param canUsersRemoteUnlock
     * @param canUsersUnlockUntil
     * @return
     */
    UserGroup createUserGroup(List<String> adminIds, List<String> lockIds, List<Schedule> schedule, List<String> userIds, boolean canUsersRemoteUnlock, boolean canUsersUnlockUntil) throws Exception;

    com.skeleton.project.dto.UserGroup createUserGroup(com.skeleton.project.dto.UserGroup userGroup);
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
