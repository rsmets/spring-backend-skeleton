package com.skeleton.project.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.Lock;
import com.skeleton.project.dto.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Object for encapsulating UserGroup request information
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupRequest {

    User requestingUser;
    List<User> targetUsers;
    List<User> targetAdmins;
    List<String> targetLockIds;
    List<KeyRelationship> keyRelationships;
    String groupId;
    String newGroupName;
    boolean needToInflate; // boolean indicating weather need to inflate (aka grab all attributes for) the user and kr lists
}
