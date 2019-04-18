package com.skeleton.project.core;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.exceptions.UserGroupPermissionsException;

public interface ICoreEngine {

	/**
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);

	com.skeleton.project.dto.entity.UserGroup createUserGroup(com.skeleton.project.dto.entity.UserGroup userGroup);

	com.skeleton.project.dto.entity.UserGroup getUserGroup(String id);

	UserGroup deleteUserGroup(String id);

	com.skeleton.project.dto.entity.UserGroup addUsersToGroup(UserGroupRequest request) throws UserGroupPermissionsException;
}


