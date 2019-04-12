package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.TreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class UserGroup {

    @JsonProperty("_id")
    @ObjectId
    String id;

    List<String> lockIds;
    List<Schedule> schedule;
    User owner;
    List<User> admins;
    List<User> users;
    KeyRelationship keyRelationship;
    String name;

    // todo put this stuff the parent class
    TreeNode groupParent;
    List<TreeNode> groupChildren;

    // special user level abilities
    boolean canRemoteUnlock;
    boolean canUnlockUntil;

    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    @JsonSetter("objectId")
    private void setObjectId(String id) {
        this.id = id;
    }

    @JsonSetter("_id")
    private void setId(String id) {
        this.id = id;
    }
}
