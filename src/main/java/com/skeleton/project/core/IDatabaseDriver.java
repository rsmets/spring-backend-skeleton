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

    /**
     * Returns a mongodb native database object. Should  never really have to be used due
     * to the powers of Morphia handling a lot of the object <-> bsjon mapping for us.
     */
    MongoDatabase getDatabase();

    /**
     * Returns old Deprecated MongoDb Object DB but MongoJack still requires it as far as I can tell.
     *
     * 4/15/19 although opting against using MongoJack (in favor of Morphia) leaving here @deprecated
     * in case it is ever useful.
     */
    @Deprecated
    DB getDB();
}
