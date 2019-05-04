package com.skeleton.project.service;

import com.skeleton.project.dto.entity.Lock;
import dev.morphia.Key;

import javax.persistence.EntityNotFoundException;

public interface ILockService {

    Key createLock(Lock lock);

    Lock modifyLock(Lock lock);

    Lock getLock(String objectId);

    Lock getLockByLockId(String lockId) throws EntityNotFoundException;

    Boolean deleteLock(Lock lock);
}
