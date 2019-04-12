package com.skeleton.project.service;

import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.UserGroup;
import dev.morphia.Key;

import java.util.List;

public interface IUserGroupService {

    /**
     * Creates a new user group
     * @param adminIds
     * @param lockIds
     * @param schedule
     * @param userIds
     * @param canUsersRemoteUnlock
     * @param canUsersUnlockUntil
     * @return
     */
    UserGroup createUserGroup(List<String> adminIds, List<String> lockIds, List<Schedule> schedule, List<String> userIds, boolean canUsersRemoteUnlock, boolean canUsersUnlockUntil) throws Exception;

    String createUserGroup(UserGroup userGroup);
    /**
     * Gets existing user group
     * @param objectId
     * @return
     */
    UserGroup getUserGroup(String objectId);

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
