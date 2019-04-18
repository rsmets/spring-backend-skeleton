package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import org.parse4j.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity()
public class Lock extends ParseObject {

    @JsonProperty("_id")
    @Id
    String id;

    String lockId;
    String shortName;
    String longName;
    int iconType;
    Integer type;

    @Property("timeZone")
    String timezone;

    Boolean claimed;
    Boolean lockUsed;
    String claimCode;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

//    State state;
//    Hub hub;
//    List<BusinessHour> businessHours;

    public static Lock convertFromDto(com.skeleton.project.dto.entity.Lock dto){
        if (dto == null)
            return null;

        Lock result = Lock.builder()
                .id(dto.getId())
                .shortName(dto.getShortName())
                .longName(dto.getLongName())
                .iconType(dto.getIconType())
                .timezone(dto.getTimezone())
                .type(dto.getType())
                .claimed(dto.getClaimed())
                .lockUsed(dto.getLockUsed())
                .claimCode(dto.getClaimCode())
                .build();

        return result;
    }

    public static List<Lock> convertFromDtos(List<com.skeleton.project.dto.entity.Lock> dtos) {
        List<Lock> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.entity.Lock dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
