package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.Pointer;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class KeyRelationship {
    @JsonProperty("_id")
    @ObjectId
    String id;

    Date endDate;
    Date startDate;
    boolean expirationDateUses;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;
    Pointer reference;
    List<Schedule> schedule;

    String pendingFirstName;
    String pendingEmailInvite;
    String smsUnlockCode;

    User user;
    Role role;
    Lock key;

    Date updatedAt;
    Date createdAt;


    public static KeyRelationship convertFromDto(com.skeleton.project.dto.KeyRelationship dto){
        if (dto == null)
            return null;

        KeyRelationship result = KeyRelationship.builder()
                .id(dto.getId())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .expirationDateUses(dto.isExpirationDateUses())
                .repeatPattern(dto.getRepeatPattern())
                .repeatType(dto.getRepeatType())
                .repeatInterval(dto.getRepeatInterval())
                .expirationDate(dto.getExpirationDate())
                .reference(dto.getReference())
                .schedule(Schedule.convertFromDtos(dto.getSchedule()))
                .pendingFirstName(dto.getPendingFirstName())
                .pendingEmailInvite(dto.getPendingEmailInvite())
                .smsUnlockCode(dto.getSmsUnlockCode())
                .user(User.convertFromDto(dto.getUser()))
                .role(Role.convertFromDto(dto.getRole()))
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<KeyRelationship> convertFromDtos(List<com.skeleton.project.dto.KeyRelationship> dtos){
        List<KeyRelationship> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.KeyRelationship dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
