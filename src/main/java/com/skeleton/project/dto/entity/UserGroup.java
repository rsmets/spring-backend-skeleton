package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class UserGroup {

    @Id
    String id;

    String name;
    String description;

    // special user level abilities
    boolean canRemoteUnlock;
    boolean canUnlockUntil;

    Set<String> lockIds = new HashSet<>();

    @Embedded("schedule")
    List<Schedule> schedule = new ArrayList<>();

    @Embedded("owner")
    User owner;

    @Embedded("admins")
    Set<User> admins = new HashSet<>();

    @Embedded("users")
    Set<User> users = new HashSet<>();

    @Embedded("keyRelationships")
    Set<KeyRelationship> keyRelationships = new HashSet<>();

    @Embedded("keyRelationshipsMap")
    Map<String, List<KeyRelationship>> keyRelationshipsMap = new HashMap<>(); // userId, kr

    // todo put this stuff the parent class
//    TreeNode groupParent;
//    List<TreeNode> groupChildren;

    // TODO with morpia 'hook' annotations...
    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

    public static String getLocksIdsAttrbibuteName() { return "lockIds"; }
    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

//    @JsonSetter("objectId")
//    private void setObjectId(String id) {
//        this.id = id;
//    }
//
//    @JsonSetter("_id")
//    private void setId(String id) {
//        this.id = id;
//    }
}
