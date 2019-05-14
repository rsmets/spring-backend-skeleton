package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
public class Schedule {

    @Property("_id")
    @Id
    String _id;

    Date endDate;
    Date startDate;
    Boolean expirationDateUsesNumOccurrences;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;
    Pointer reference;

    @Property("_updated_at") //RJS not sure why this annotation is not working and needed actually name the variable accordingly
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;
//    @JsonProperty("_updated_at") //RJS not sure why this annotation is not working and needed actually name the variable accordingly
//    Date _updated_at;
//    @JsonProperty("_created_at")
//    Date _created_at;


    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    @JsonSetter("objectId")
    public void setObjectId(String id) {
        this._id = id;
    }

    @JsonSetter("_id")
    public void setId(String id) {
        this._id = id;
    }

    @JsonGetter("_id")
    public String getId() {
        return this._id;
    }

}
