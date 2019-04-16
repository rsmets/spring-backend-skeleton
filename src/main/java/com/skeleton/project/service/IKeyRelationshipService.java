package com.skeleton.project.service;

import com.skeleton.project.domain.KeyRelationship;
import dev.morphia.Key;

import java.util.List;

public interface IKeyRelationshipService {

    Key createKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship modifyKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship getKeyRelationship(String objectId);

    List<KeyRelationship> findKeyRelationshipsByUser(String userObjectId);

    Boolean deleteKeyRelationship(KeyRelationship keyRelationship);
}
