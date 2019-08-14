package com.skeleton.project.core;

import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;

/**
 * Database interface, interface
 */
public interface IDatabaseDriver {

    /**
     * A Morphia (our ORM) method which handles mapping the Annotation based DTO Pojos
     * to our our document based db
     */
    Datastore getDatastore();
}
