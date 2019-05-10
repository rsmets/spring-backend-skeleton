package com.skeleton.project.service;

import com.skeleton.project.dto.entity.KeyRelationship;
import dev.morphia.Key;

import java.util.List;

public interface IKeyRelationshipService {

    Key createKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship modifyKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship getKeyRelationship(String objectId);

    List<KeyRelationship> getKeyRelationshipsByUser(String userObjectId);

    List<KeyRelationship> getKeyRelationshipsByUserAndGroup(String userObjectId, String groupId);

    KeyRelationship getKeyRelationship(String userObjectId, String lockObjectId);

    Boolean deleteKeyRelationship(KeyRelationship keyRelationship);
}
