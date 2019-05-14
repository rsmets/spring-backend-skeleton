package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the User collection
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity("_User")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Property("_id")
    @Id
    @EqualsAndHashCode.Include // id is the only attribute used for equals and hashcode methods
    String _id; //todo convert back to just id;

    String primaryEmail;
    String username;
    String lastName;
    String firstName;
    String primaryPhone;
    int type;

    // Can not get the pointer from parse world to play simultaneously nice with the json blob object world ignoring for now
//    @Embedded
//    List<Email> emails;
//    @Embedded
//    List<Pointer> phones;

//    String password;
//    String emailCode;
//    String smsCode;
//    String APINumber;
//    Boolean emailVerified;
//    Boolean verified;
//    Boolean primary;
//    Date updatedAt;
//    Date createdAt;

    public static String getAttributeName() { return "user"; }
    public static String getAttributeNamePlural() { return "users"; }

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