package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.List;

/**
 * Data Transfer Object for the User collection
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class User {

    @JsonProperty("_id")
    @ObjectId
    @Id
    String id;

    String primaryEmail;
    String username;
    String lastName;
    String firstName;
    String primaryPhone;
    int type;


    List<Email> emails;
    List<Phone> phones;

//    String password;
//    String emailCode;
//    String smsCode;
//    String APINumber;
//    Boolean emailVerified;
//    Boolean verified;
//    Boolean primary;
//    Date updatedAt;
//    Date createdAt;
}