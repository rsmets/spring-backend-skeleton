package com.skeleton.project.domain;

import com.fasterxml.jackson.core.TreeNode;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Builder;
import lombok.Data;
import org.mongojack.ObjectId;

import java.util.*;

@Data
@Builder
@Entity
public class UserGroup { //todo extend an abstract group class that has a notion of a tree node

    @ObjectId
    @Id
    org.bson.types.ObjectId id;

    User owner;
    String name;
    String description;

    Set<String> lockIds = Collections.emptySet();
    List<Schedule> schedule = Collections.emptyList();
    Set<User> admins = Collections.emptySet();
    Set<User> users = Collections.emptySet();
    Set<KeyRelationship> keyRelationships;

    @Embedded("keyRelationshipsMap")
    Map<User, KeyRelationship> keyRelationshipsMap = new HashMap<>();

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

    public static UserGroup convertFromDto(com.skeleton.project.dto.entity.UserGroup dto){
        if (dto == null)
            return null;

        UserGroup result = UserGroup.builder()
                .id(new org.bson.types.ObjectId(dto.getId()))
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
                .description(dto.getDescription())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();

        return result;
    }

    // RJS I know there is away to abstract this... but going the ugly route for now
    public static List<UserGroup> convertFromDtos(List<com.skeleton.project.dto.entity.UserGroup> dtos){
        List<UserGroup> result = new ArrayList<>();

        if (dtos == null)
            return result;

        for (com.skeleton.project.dto.entity.UserGroup dto : dtos) {
            result.add(convertFromDto(dto));
        }

        return result;
    }

    // RJS not worth the effort will just pass id's back for now. Hopefully can implement GRPC sooner than later to get around this.
//    public static UserGroup convertToDto(UserGroup obj){
//        if (obj == null)
//            return null;
//
//        UserGroup result = new UserGroup();
//        result.setId(obj.getId());
//        result.setLockIds(obj.getLockIds());
//        result.setSchedule(obj.getSchedule());
//        result.setOwner(obj.getOwner());
//        result.setAdmins(obj.getAdmins());
//        result.setUsers(obj.getUsers());
//        result.setKeyRelationships(obj.getKeyRelationships());
//        result.setCanRemoteUnlock(obj.isCanRemoteUnlock());
//        result.setCanUnlockUntil(obj.isCanUnlockUntil());
//        result.setName(obj.getName());
//        result.setCreatedAt(obj.getCreatedAt());
//        result.setUpdatedAt(obj.getUpdatedAt());
//
//        return result;
//    }
}
