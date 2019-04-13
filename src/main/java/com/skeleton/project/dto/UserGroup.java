package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.TreeNode;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class UserGroup {

    @Property("_id")
    @Id
    @ObjectId
    String id;

    List<String> lockIds = Collections.emptyList();

    @Embedded
    List<Schedule> schedule = Collections.emptyList();;

    @Embedded
    User owner;

    @Embedded
    List<User> admins = Collections.emptyList();;

    @Embedded
    List<User> users = Collections.emptyList();;

    @Embedded
    KeyRelationship keyRelationship;
    String name;

    // todo put this stuff the parent class
//    TreeNode groupParent;
//    List<TreeNode> groupChildren;

    // special user level abilities
    Boolean canRemoteUnlock;
    Boolean canUnlockUntil;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

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
