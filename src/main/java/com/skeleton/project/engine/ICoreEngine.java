package com.skeleton.project.engine;

import com.skeleton.project.domain.BaseResponse;

import java.util.List;

public interface ICoreEngine {

	/**
	 *
	 * @param example
	 * @return List of {@link BaseResponse}
	 */
	List<BaseResponse> executeAction(Object example);
}


