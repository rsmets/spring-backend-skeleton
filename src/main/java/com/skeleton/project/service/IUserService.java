package com.skeleton.project.service;

import com.skeleton.project.dto.User;
import dev.morphia.Key;

public interface IUserService {

    Key createUser(User user);

    User getUser(final String objectId);

    User getUserByPhone(final String phoneNumber);
}
