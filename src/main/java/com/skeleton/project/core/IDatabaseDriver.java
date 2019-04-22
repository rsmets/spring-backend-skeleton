package com.skeleton.project.core;

import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;

/**
 * Database interface, interface
 */
public interface IDatabaseDriver {

    MongoDatabase getDatabase();

    @Deprecated
    DB getDB();
}
