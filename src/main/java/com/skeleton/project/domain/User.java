package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
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

    public static User convertFromDto(com.skeleton.project.dto.User dto){
        if (dto == null)
            return null;

        User result = User.builder()
                .id(dto.getId())
                .primaryEmail(dto.getPrimaryEmail())
                .username(dto.getUsername())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .primaryPhone(dto.getPrimaryPhone())
                .type(dto.getType())
                .emails(Email.convertFromDtos(dto.getEmails()))
                .phones(Phone.convertFromDtos(dto.getPhones()))
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<User> convertFromDtos(List<com.skeleton.project.dto.User> dtos){
        List<User> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.User dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
