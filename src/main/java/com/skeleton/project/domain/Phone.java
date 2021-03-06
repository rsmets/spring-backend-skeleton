package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Data;
import org.parse4j.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class Phone extends ParseObject {
    @JsonProperty("_id")
    @Id
    String id;

    String phone;
    String verificationCode;
    Boolean primary;
    Boolean verified;
    User user;

//    ValidationInfo validationInfo;
    Date updatedAt;
    Date createdAt;

    public static Phone convertFromDto(com.skeleton.project.dto.entity.Phone dto){
        if (dto == null)
            return null;

        Phone result = Phone.builder()
                .id(dto.get_id())
                .phone(dto.getPhone())
                .verificationCode(dto.getVerificationCode())
                .primary(dto.getPrimary())
                .verified(dto.getVerified())
                .user(dto.getUser() != null ? User.convertFromDto(dto.getUser()) : null)
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<Phone> convertFromDtos(List<com.skeleton.project.dto.entity.Phone> dtos){
        List<Phone> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.entity.Phone dto : dtos) {
            result.add(convertFromDto(dto));
        }


        return result;
    }
}
