package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
public class Lock {

    @Id
    @Property("_id")
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
