package com.skeleton.project.service;

import com.skeleton.project.dto.KeyRelationship;
import dev.morphia.Key;

import java.util.List;

public interface IKeyRelationshipService {

    Key createKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship modifyKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship getKeyRelationship(String objectId);

    List<KeyRelationship> getKeyRelationshipsByUser(String userObjectId);

    KeyRelationship getKeyRelationship(String userObjectId, String lockObjectId);

    Boolean deleteKeyRelationship(KeyRelationship keyRelationship);
}
