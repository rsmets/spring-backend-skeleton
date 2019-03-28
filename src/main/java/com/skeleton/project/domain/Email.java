package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Email {
    @JsonProperty("_id")
    @ObjectId
    String id;

    String email;
    Boolean primary;
    String verificationCode;
    Boolean verified;
    User user;

//    Date updatedAt;
//    Date createdAt

    public static Email convertFromDto(com.skeleton.project.dto.Email dto){
        Email result = Email.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .verificationCode(dto.getVerificationCode())
                .primary(dto.getPrimary())
                .verified(dto.getVerified())
                .user(dto.getUser() != null ? User.convertFromDto(dto.getUser()) : null)
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<Email> convertFromDtos(List<com.skeleton.project.dto.Email> dtos){
        List<Email> result = new ArrayList<>();

        for (com.skeleton.project.dto.Email dto : dtos) {
            result.add(convertFromDto(dto));
        }


        return result;
    }
}
