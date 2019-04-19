package com.skeleton.project.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class KeyRelationship {

    @Property("_id")
    @Id
    String id;

    Date endDate;
    Date startDate;

    boolean expirationDateUses;
    List<Integer> repeatPattern; // todo make object
    Integer repeatType;
    Integer repeatInterval;
    Date expirationDate;
    Pointer reference;

    @Embedded
    List<Schedule> schedule;

    String pendingFirstName;
    String pendingEmailInvite;
    String smsUnlockCode;
    String userGroupId;

    // RJS tried to use mongo dbRef object but struggled to get to decode... the id will do for grabs / tracking.
//    @Reference
//    UserGroup userGroup;

    @Embedded
    User user;

    @Embedded
    Role role;
    @Property("_p_role")
    String roleObjectId;
    @Embedded
    Lock key;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

    public static String getAttributeName() { return "keyRelationship"; }
    public static String getAttributeNamePlural() { return "keyRelationships"; }

    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

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
}
