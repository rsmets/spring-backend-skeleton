package com.skeleton.project.service;

import com.skeleton.project.domain.User;

public interface IUserService {

    User createUser(final String email, final String username, final String lastName, final String firstName, final String phone, final int type);

    User getUser(final String objectId);
}
