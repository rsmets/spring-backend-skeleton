package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.TreeNode;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
public class UserGroup { //todo extend an abstract group class that has a notion of a tree node

    @JsonProperty("_id")
    @ObjectId
    @Id
    String id;

    List<String> lockIds;
    Schedule schedule;
    User owner;
    List<User> admins;
    List<User> users;
    KeyRelationship keyRelationship;

    // todo put this stuff the parent class
    TreeNode groupParent;
    List<TreeNode> groupChildren;

    // special user level abilities
    boolean canRemoteUnlock;
    boolean canUnlockUntil;

    public static UserGroup convertFromDto(com.skeleton.project.dto.UserGroup dto){
        UserGroup result = UserGroup.builder()
                .id(dto.getId())
                .lockIds(dto.getLockIds())
                .schedule(Schedule.convertFromDto(dto.getSchedule()))
                .owner(User.convertFromDto(dto.getOwner()))
                .admins(User.convertFromDtos(dto.getAdmins()))
                .users(User.convertFromDtos(dto.getUsers()))
                .keyRelationship(KeyRelationship.convertFromDto(dto.getKeyRelationship()))
                .groupParent(dto.getGroupParent())
                .groupChildren(dto.getGroupChildren())
                .canRemoteUnlock(dto.isCanRemoteUnlock())
                .canUnlockUntil(dto.isCanRemoteUnlock())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<UserGroup> convertFromDtos(List<com.skeleton.project.dto.UserGroup> dtos){
        List<UserGroup> result = new ArrayList<>();

        for (com.skeleton.project.dto.UserGroup dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
