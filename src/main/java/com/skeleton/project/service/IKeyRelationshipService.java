package com.skeleton.project.service;

import com.skeleton.project.domain.KeyRelationship;
import dev.morphia.Key;

public interface IKeyRelationshipService {

    Key createKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship modifyKeyRelationship(KeyRelationship keyRelationship);

    KeyRelationship getKeyRelationshp(String objectId);

    Boolean deleteKeyRelationship(KeyRelationship keyRelationship);
}
