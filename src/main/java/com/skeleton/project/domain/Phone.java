package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

@Data
@Builder
public class Phone {
    @JsonProperty("_id")
    @ObjectId
    String id;

    String phone;
    String verificationCode;
    Boolean primary;
    Boolean verified;
    User user;

//    ValidationInfo validationInfo;
//    Date updatedAt;
//    Date createdAt;

    public static Phone convertFromDto(com.skeleton.project.dto.Phone dto){
        Phone result = Phone.builder()
                .id(dto.getId())
                .phone(dto.getPhone())
                .verificationCode(dto.getVerificationCode())
                .primary(dto.getPrimary())
                .verified(dto.getVerified())
                .user(dto.getUser())
                .build();

        return result;
    }
}
