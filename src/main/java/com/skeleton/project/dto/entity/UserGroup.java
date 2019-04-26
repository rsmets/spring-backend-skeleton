package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class UserGroup {

//    @Property("_id")
    @Id
    String id;

    List<String> lockIds = Collections.emptyList();

    @Embedded("schedule")
    List<Schedule> schedule = Collections.emptyList();

    @Embedded("owner")
    User owner;

    @Embedded("admins")
    Set<User> admins = Collections.emptySet();

    @Embedded("users")
    Set<User> users = Collections.emptySet();

    @Embedded("keyRelationships")
    Set<KeyRelationship> keyRelationships; // TODO make Map<String, Set<KeyRelationships>, a lockId -> kr map

    String name;

    // todo put this stuff the parent class
//    TreeNode groupParent;
//    List<TreeNode> groupChildren;

    // special user level abilities
    boolean canRemoteUnlock;
    boolean canUnlockUntil;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

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
