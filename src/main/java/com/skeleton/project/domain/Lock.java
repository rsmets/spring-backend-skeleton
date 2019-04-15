package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.parse4j.ParseObject;

@Data
public class Lock extends ParseObject {

    @JsonProperty("_id")
    @Id
    String id;

    String lockId;
    String shortName;
    String longName;
    int iconType;
    String timezone;
    Integer type;

    Boolean claimed;
    Boolean lockUsed;
    String claimCode;

//    State state;
//    Hub hub;
//    List<BusinessHour> businessHours;
}
