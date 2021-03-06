package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Data;
import org.parse4j.ParseObject;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Role extends ParseObject {

    @JsonProperty("_id")
    @Id
    String id;

    String name;
    int roleId;

    public static Role convertFromDto(com.skeleton.project.dto.entity.Role dto){
        if (dto == null)
            return null;

        Role result = Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .roleId(dto.getRoleID())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<Role> convertFromDtos(List<com.skeleton.project.dto.entity.Role> dtos){
        List<Role> result = new ArrayList<>();

        for (com.skeleton.project.dto.entity.Role dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
