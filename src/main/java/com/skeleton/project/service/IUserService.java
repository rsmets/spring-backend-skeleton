package com.skeleton.project.service;

import com.skeleton.project.domain.User;
import dev.morphia.Key;

public interface IUserService {

    Key createUser(final String email, final String username, final String lastName, final String firstName, final String phone, final int type);

    Key createUser(User user);

    User getUser(final String objectId);

    User getUserByPhone(final String phoneNumber);
}
