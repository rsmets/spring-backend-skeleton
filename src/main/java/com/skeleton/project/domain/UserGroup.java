package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.TreeNode;
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
public class UserGroup {
//@ParseClassName("UserGroup")
//public class UserGroup extends ParseObject { //todo extend an abstract group class that has a notion of a tree node

    @ObjectId
    @Id
    org.bson.types.ObjectId id;

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

    // RJS not worth the effort will just pass id's back for now. Hopefully can implement GRPC sooner than later to get around this.
//    public static com.skeleton.project.dto.UserGroup convertToDto(UserGroup obj){
//        if (obj == null)
//            return null;
//
//        com.skeleton.project.dto.UserGroup result = new com.skeleton.project.dto.UserGroup();
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
