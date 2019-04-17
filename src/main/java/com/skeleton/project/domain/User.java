package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.Pointer;
import com.skeleton.project.utils.Utils;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;
import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;
import org.parse4j.ParseUser;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity("_User")
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


//    @Embedded
//    List<Email> emails;
//
//    @Embedded
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

    public static User convertFromDto(com.skeleton.project.dto.User dto){
        if (dto == null)
            return null;

        // Need to inflate the Pointers (Emails and Phones)
        // Opting to not do the full obj grab because pretty sure not needed. Just populating the objectIds
        List<Phone> phones = new ArrayList<>();
        List<Email> emails = new ArrayList<>();

        //TODO RJS need to figure out a way to handle the parse list of pointers vs the mongo list object json blobs...
        // Pointer does not work with the actual email object

//        for (Pointer point : Utils.nullGuard(dto.getPhones())) {
//            Phone phone = Phone.builder().id(point.getObjectId()).build();
//            phones.add(phone);
//        }
//
//        for (com.skeleton.project.dto.Email point : Utils.nullGuard(dto.getEmails())) {
//            Email email = Email.builder().id(point.getObjectId()).build();
//            if (point.getObjectId() == null) {
//                email = Email.builder().id(point.getId()).build();
//            }
////            Email email = Email.builder().id(point.getObjectId()).build();
////            Email email = Email.builder().id(point.getId()).build();
//            emails.add(email);
//        }

        User result = User.builder()
                .id(dto.getId())
                .primaryEmail(dto.getPrimaryEmail())
                .username(dto.getUsername())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .primaryPhone(dto.getPrimaryPhone())
                .type(dto.getType())
//                .emails(emails)
//                .phones(phones)
//                .emails(Email.convertFromDtos(dto.getEmails()))
//                .phones(Phone.convertFromDtos(dto.getPhones()))
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
