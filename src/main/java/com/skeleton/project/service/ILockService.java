package com.skeleton.project.service;

import com.skeleton.project.dto.Lock;
import dev.morphia.Key;

public interface ILockService {

    Key createLock(Lock lock);

    Lock modifyLock(Lock lock);

    Lock getLock(String objectId);

    Lock getLockByLockId(String lockId);

    Boolean deleteLock(Lock lock);
}
