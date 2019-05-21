package com.skeleton.project.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Object for encapsulating UserGroup request information
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupRequest {

    User requestingUser;
    List<User> targetUsers;
    Set<User> targetAdmins;
    List<String> targetLockIds;
    Set<KeyRelationship> keyRelationships;
    List<Schedule> schedule;
    Map<String, List<KeyRelationship>> keyRelationshipsMap;
    String groupId;
    String newGroupName;
    boolean needToInflate; // boolean indicating weather need to inflate (aka grab all attributes for) the user and kr lists
    boolean administrativeAccessOnly;
}
