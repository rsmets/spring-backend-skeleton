package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.skeleton.project.dto.entity.Pointer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity("Schedule")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Schedule {

    @Property("_id")
    @Id
    @EqualsAndHashCode.Include // id is the only attribute used for equals and hashcode methods
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

    @JsonSetter("objectId")
    public void setObjectId(String id) {
        this.id = id;
    }

    @ObjectId
    @JsonSetter("_id")
    public void setId(String id) {
        this.id = id;
    }

    @ObjectId
    @JsonSetter("_id")
    public String getId() { return this.id;}

    public static Schedule convertFromDto(com.skeleton.project.dto.entity.Schedule dto){
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
    public static List<Schedule> convertFromDtos(List<com.skeleton.project.dto.entity.Schedule> dtos){
        List<Schedule> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.entity.Schedule dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }

}
