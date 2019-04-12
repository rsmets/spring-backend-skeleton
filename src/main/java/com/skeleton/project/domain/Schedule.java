package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.Pointer;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;
import org.parse4j.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class Schedule extends ParseObject {

    @JsonProperty("_id")
    @ObjectId
    String id;

    Date endDate;
    Date startDate;
    boolean expirationDateUsesNumOccurrences;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;

    // could point to a KeyRelationshipService or UserGroup object. Maybe those classes should extend abstract reference?
    Pointer reference;

    Date updatedAt;
    Date createdAt;

    public static Schedule convertFromDto(com.skeleton.project.dto.Schedule dto){
        if (dto == null)
            return null;

        Schedule result = Schedule.builder()
                .id(dto.get_id())
                .reference(dto.getReference())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .expirationDateUsesNumOccurrences(dto.getExpirationDateUsesNumOccurrences())
                .repeatPattern(dto.getRepeatPattern())
                .repeatInterval(dto.getRepeatInterval())
                .expirationDate(dto.getExpirationDate())
                .updatedAt(dto.get_updated_at())
                .createdAt(dto.get_created_at())
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
