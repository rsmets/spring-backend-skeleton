package com.skeleton.project.service;

import com.mongodb.DBCollection;
import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.UserGroup;
import com.skeleton.project.dto.KeyRelationship;
import com.skeleton.project.engine.DatabaseDriver;
import dev.morphia.Key;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleService implements IScheduleService {

    @Autowired
    DatabaseDriver _database;

    @Override
    public Key createSchedule(Schedule schedule) {
        return createScheduleWithMorphia(schedule);
    }

    @Override
    public Schedule modifySchedule(Schedule schedule) {
        return null;
    }

    @Override
    public Schedule getSchedule(String objectId) {
        return getScheduleWithMongoJack(objectId);
    }

    @Override
    public Boolean deleteSchedule(String objectId) {
        return null;
    }

    private Key createScheduleWithMorphia(Schedule schedule) {
        Key key = _database.getDatastore().save(schedule);

        return key;
    }

    private Schedule getScheduleWithMongoJack(String objectId){
        DBCollection krCollection = _database.getDB().getCollection("Schedule");
        JacksonDBCollection<com.skeleton.project.dto.Schedule, String> collection = JacksonDBCollection.wrap(krCollection, com.skeleton.project.dto.Schedule.class, String.class);
        com.skeleton.project.dto.Schedule schedule = collection.findOneById(objectId);

        log.info("key relationship from jacksonified db: " + schedule);

        return Schedule.convertFromDto(schedule);
    }
}
