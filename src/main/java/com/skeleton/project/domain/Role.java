package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Role {

    @JsonProperty("_id")
    @ObjectId
    String id;

    String name;
    int roleId;


    public static Role convertFromDto(com.skeleton.project.dto.Role dto){
        if (dto == null)
            return null;

        Role result = Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .roleId(dto.getRoleId())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<Role> convertFromDtos(List<com.skeleton.project.dto.Role> dtos){
        List<Role> result = new ArrayList<>();

        for (com.skeleton.project.dto.Role dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
