package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skeleton.project.dto.Pointer;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class KeyRelationship {
    @JsonProperty("_id")
    @ObjectId
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
}
