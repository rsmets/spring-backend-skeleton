package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Lock {

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


    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    @JsonSetter("objectId")
    private void setObjectId(String id) {
        this.id = id;
    }

    @JsonSetter("_id")
    private void setId(String id) {
        this.id = id;
    }
}