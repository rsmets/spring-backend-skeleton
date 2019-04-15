package com.skeleton.project.engine;

import com.skeleton.project.domain.BaseResponse;
import com.skeleton.project.domain.UserGroup;

public interface ICoreEngine {

	/**
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);

	UserGroup createUserGroup(UserGroup userGroup);

	UserGroup getUserGroup(String id);

	UserGroup deleteUserGroup(String id);
}


