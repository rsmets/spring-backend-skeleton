package com.skeleton.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.core.DatabaseDriver;
import dev.morphia.Key;
import dev.morphia.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.mongojack.JacksonDBCollection;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        return getWithMorphia(objectId);
//        return getScheduleWithMongoJack(objectId);
//        return getWithParse(objectId);
    }

    @Override
    public Boolean deleteSchedule(String objectId) {
        return null;
    }

    private Key createScheduleWithMorphia(Schedule schedule) {
        Key key = _database.getDatastore().save(schedule);

        return key;
    }

    private Schedule getWithMorphia(String objectId) {
        final Query<Schedule> query = _database.getDatastore().createQuery(Schedule.class);

        final Schedule schedule = query
                .field("_id").equalIgnoreCase(objectId)
                .get(); //todo figure out how to query for one.

        log.info("Got schedule with id " + objectId + ": " + schedule);

        return schedule; //rjs this should always be one entry
    }

    private Schedule getWithParse(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        try {
            ParseObject result = query.get(objectId);
            if (result != null)
                log.info("doc from parseified db: " + result.toString());
            else
                log.warn("no dice getting any results");

            log.info(result.toString());
            log.info(result.getParseData().toString());

            Schedule dto = new ObjectMapper().readValue(result.getParseData().toString(), Schedule.class);
            return dto;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
//        query.getInBackground(objectId, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject result, ParseException e) {
//                if (e == null) {
//                    if (result != null)
//                        log.info("doc from parseified db: " + result.toString());
//                    else
//                        log.warn("no dice getting any results");
//                } else {
//                    // something went wrong
//                    log.error("Something went wrong", e);
//                }
//            }
//        });

        return null;
    }
}
