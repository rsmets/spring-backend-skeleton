package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.mongojack.ObjectId;

@Data
public class Lock {
    @JsonProperty("_id")
    @ObjectId
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
