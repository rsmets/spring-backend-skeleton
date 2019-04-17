package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;
import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;

import java.util.*;

@Data
@Builder
@Entity
@ParseClassName("UserGroup")
public class UserGroup extends ParseObject { //todo extend an abstract group class that has a notion of a tree node

    @ObjectId
    @Id
    String id;

    User owner;
    String name;

    List<String> lockIds = Collections.emptyList();
    List<Schedule> schedule = Collections.emptyList();
    List<User> admins = Collections.emptyList();
    List<User> users = Collections.emptyList();
    List<KeyRelationship> keyRelationships;

    @Property("_updated_at")
    Date updatedAt;
    @Property("_created_at")
    Date createdAt;

    // todo put this stuff the parent class
    TreeNode groupParent;
    List<TreeNode> groupChildren;

    // special user level abilities
    boolean canRemoteUnlock;
    boolean canUnlockUntil;

    public static UserGroup convertFromDto(com.skeleton.project.dto.UserGroup dto){
        if (dto == null)
            return null;

        UserGroup result = UserGroup.builder()
                .id(dto.getId())
                .lockIds(dto.getLockIds())
                .schedule(Schedule.convertFromDtos(dto.getSchedule()))
                .owner(User.convertFromDto(dto.getOwner()))
                .admins(User.convertFromDtos(dto.getAdmins()))
                .users(User.convertFromDtos(dto.getUsers()))
                .keyRelationships(KeyRelationship.convertFromDtos(dto.getKeyRelationships()))
//                .groupParent(dto.getGroupParent())
//                .groupChildren(dto.getGroupChildren())
                .canRemoteUnlock(dto.isCanRemoteUnlock())
                .canUnlockUntil(dto.isCanUnlockUntil())
//                .canRemoteUnlock(dto.getCanRemoteUnlock())
//                .canUnlockUntil(dto.getCanUnlockUntil())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<UserGroup> convertFromDtos(List<com.skeleton.project.dto.UserGroup> dtos){
        List<UserGroup> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.UserGroup dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }
}
