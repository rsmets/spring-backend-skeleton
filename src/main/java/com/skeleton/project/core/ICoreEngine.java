package com.skeleton.project.core;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.UserGroup;

public interface ICoreEngine {

	/**
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);

	com.skeleton.project.dto.UserGroup createUserGroup(com.skeleton.project.dto.UserGroup userGroup);

	com.skeleton.project.dto.UserGroup getUserGroup(String id);

	UserGroup deleteUserGroup(String id);
}


