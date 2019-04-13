package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;
import org.parse4j.ParseClassName;

import java.util.List;

/**
 * Data Transfer Object for the User collection
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity("_User")
public class User {

    @Property("_id")
    @Id
    String _id; //todo convert back to just id;

    String primaryEmail;
    String username;
    String lastName;
    String firstName;
    String primaryPhone;
    int type;

    @Embedded
    List<Pointer> emails;
    @Embedded
    List<Pointer> phones;

//    String password;
//    String emailCode;
//    String smsCode;
//    String APINumber;
//    Boolean emailVerified;
//    Boolean verified;
//    Boolean primary;
//    Date updatedAt;
//    Date createdAt;

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