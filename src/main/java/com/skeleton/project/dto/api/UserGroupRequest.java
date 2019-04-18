package com.skeleton.project.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    String groupId;
    boolean needToInflate; // boolean indicating weather need to inflate (aka grab all attributes for) the user list

}
