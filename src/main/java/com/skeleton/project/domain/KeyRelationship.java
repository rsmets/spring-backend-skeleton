package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.entity.Pointer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.*;

@Data
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL) // lots of null by design. May want to remove if the node world or client side is expecting some
public class KeyRelationship {

    @JsonProperty("_id")
    @Property("_id")
    @ObjectId
    @Id
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
    String userGroupId;

//    UserGroup userGroup;

    User user;
    Role role;
    Lock key;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

    public static KeyRelationship convertFromDto(com.skeleton.project.dto.entity.KeyRelationship dto){
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
//                .role(Role.convertFromDto(dto.getRoleObjectId()))  //RJS due to the pointer being a string would need to TODO roleService todo db retrieval
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .userGroupId(dto.getUserGroupId())
//                .userGroup(UserGroup.convertFromDto(dto.getUserGroup())) // RJS tried to use mongo dbRef object but struggled to get to decode... the id will do for grabs / tracking.
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static Set<KeyRelationship> convertFromDtos(Set<com.skeleton.project.dto.entity.KeyRelationship> dtos){
        Set<KeyRelationship> result = new HashSet<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.entity.KeyRelationship dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
