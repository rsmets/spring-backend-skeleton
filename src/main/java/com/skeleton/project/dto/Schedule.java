package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Schedule {

    @ObjectId
    @JsonProperty("_id")
    String _id;

    Date endDate;
    Date startDate;
    Boolean expirationDateUsesNumOccurrences;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;
    Pointer reference;

    @JsonProperty("_updated_at") //RJS not sure why this annotation is not working and needed actually name the variable accordingly
    Date _updated_at;
    @JsonProperty("_created_at")
    Date _created_at;


    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

//    @JsonSetter("objectId")
//    public void setObjectId(String id) {
//        this.id = id;
//    }
//
//    @JsonSetter("_id")
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    @JsonGetter("_id")
//    public String getId() {
//        return this.id;
//    }

}
