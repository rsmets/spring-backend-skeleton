package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class KeyRelationship {

    @ObjectId
    @JsonProperty("_id")
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
    List<Schedule> schedule;

    String pendingFirstName;
    String pendingEmailInvite;
    String smsUnlockCode;

    User user;
    Role role;
    Lock key;

    Date updatedAt;
    Date createdAt;

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
