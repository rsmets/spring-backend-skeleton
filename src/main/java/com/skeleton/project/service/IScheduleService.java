package com.skeleton.project.service;

import com.skeleton.project.dto.entity.Schedule;
import dev.morphia.Key;

public interface IScheduleService {

    Key createSchedule(Schedule schedule);

    Schedule modifySchedule(Schedule schedule);

    Schedule getSchedule(String objectId);

    Boolean deleteSchedule(String objectId);
}
