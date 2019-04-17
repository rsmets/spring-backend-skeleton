package com.skeleton.project.service;

import com.skeleton.project.domain.Lock;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LockService implements ILockService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public Key createLock(Lock lock) {
        return null;
    }

    @Override
    public Lock modifyLock(Lock lock) {
        return null;
    }

    @Override
    public Lock getLock(String objectId) {
        return null;
    }

    @Override
    public Lock getLockByLockId(String lockId) {
        final Query<com.skeleton.project.dto.Lock> query = _database.getDatastore().createQuery(com.skeleton.project.dto.Lock.class);


        final com.skeleton.project.dto.Lock lock = query
                .disableValidation()
                .field("lockId").equal(lockId)
                .get();

        log.info("Got lock with id " + lockId + ": " + lock);

        com.skeleton.project.domain.Lock result = com.skeleton.project.domain.Lock.convertFromDto(lock);
        return result;
    }

    @Override
    public Boolean deleteLock(Lock lock) {
        return null;
    }
}