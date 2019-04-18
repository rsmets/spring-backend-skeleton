package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class Email {

    @Property("_id")
    @Id
    String _id;

    String email;
    Boolean primary;
    String verificationCode;
    Boolean verified;
    User user;

    Date _updated_at;
    Date _created_at;

    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    String objectId;
    String className;

//    @JsonSetter("objectId")
//    public void setObjectId(String id) {
//        this.id = id;
//    }
//
//    @JsonGetter("objectId")
//    public String getObjectId() {
//        return this.id;
//    }

    @JsonSetter("_id")
    public void setId(String id) {
        this._id = id;
    }

    @JsonGetter("_id")
    public String getId() {
        return this._id;
    }
}

