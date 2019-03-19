package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("_id")
    @ObjectId
    String id;

    String primaryEmail;
    String username;
    String lastName;
    String firstName;
    String primaryPhone;
    int type;


//    List<Email> emails;
//    List<Phone> phones;
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
