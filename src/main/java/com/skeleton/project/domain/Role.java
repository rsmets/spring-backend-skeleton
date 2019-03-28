package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.mongojack.ObjectId;

@Data
public class Role {

    @JsonProperty("_id")
    @ObjectId
    String id;

    String name;
    int roleId;
}
