package com.skeleton.project.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skeleton.project.dto.entity.UserGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupResponse {

    String id;
    String name;
    String userType; // todo reference the Role enum

}
