package com.skeleton.project.engine;

import com.skeleton.project.domain.BaseResponse;

public interface ICoreEngine {

	/**
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	BaseResponse executeAction(Object example);
}


