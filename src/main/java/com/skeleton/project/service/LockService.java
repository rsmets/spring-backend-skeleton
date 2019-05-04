package com.skeleton.project.service;

import com.skeleton.project.dto.entity.Lock;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
    public Lock getLockByLockId(String lockId) throws EntityNotFoundException{
        final Query<Lock> query = _database.getDatastore().createQuery(Lock.class);


        final Lock lock = query
                .disableValidation()
                .field("lockId").equal(lockId)
                .get();

        if(lock == null) {
            String message = "Requested lock with id " + lockId + " does not exist";
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        log.info("Got lock with id " + lockId + ": " + lock);

        return lock;
    }

    @Override
    public Boolean deleteLock(Lock lock) {
        return null;
    }
}
