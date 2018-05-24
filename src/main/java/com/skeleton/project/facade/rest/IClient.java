package com.skeleton.project.facade.rest;

import com.skeleton.project.domain.BaseResponse;

public interface IClient {

    /**
     *
     * @param example
     * @return
     */
    BaseResponse doAction(Object example);
}
