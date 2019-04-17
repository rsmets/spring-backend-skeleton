package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.Pointer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;
import org.parse4j.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity("Schedule")
public class Schedule {

    @JsonProperty("_id")
    @ObjectId
    @Property("_id")
    @Id
    String id;

    Date endDate;
    Date startDate;
    Boolean expirationDateUsesNumOccurrences;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;

    // could point to a KeyRelationshipService or UserGroup object. Maybe those classes should extend abstract reference?
    Pointer reference;


    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

    public static Schedule convertFromDto(com.skeleton.project.dto.Schedule dto){
        if (dto == null)
            return null;

        Schedule result = Schedule.builder()
                .id(dto.getId())
                .reference(dto.getReference())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .expirationDateUsesNumOccurrences(dto.getExpirationDateUsesNumOccurrences())
                .repeatPattern(dto.getRepeatPattern())
                .repeatInterval(dto.getRepeatInterval())
                .expirationDate(dto.getExpirationDate())
//                .updatedAt(dto.get_updated_at())
//                .createdAt(dto.get_created_at())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .repeatType(dto.getRepeatType())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<Schedule> convertFromDtos(List<com.skeleton.project.dto.Schedule> dtos){
        List<Schedule> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.Schedule dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }

}
