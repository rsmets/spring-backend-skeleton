package com.skeleton.project.core;

import com.mongodb.DB;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@PropertySource("classpath:application.properties") // Not necessary but leaving for explicit declaration sake
public class DatabaseDriver implements IDatabaseDriver{

    @Value("${db.host}")
    private String _dbHost;

    @Value("${db.port}")
    private int _dbPort;

    @Value("${db.name}")
    private String _dbName;

    @Value("${db.user:}") // defaults to empty string
    private String _dbUser;

    @Value("${db.pw:}") // defaults to empty string
    private String _dbPw;

    private MongoDatabase _database;
    private com.mongodb.MongoClient _mongoClient;
    private Datastore _datastore;
    private final Morphia morphia = new Morphia();
    private DB _db;

    /**
     * @see IDatabaseDriver#getDatastore()
     */
    @Override
    public Datastore getDatastore() {
        if (_mongoClient == null) {
            // TODO should really make various implementation based on the spring profile...
            if (StringUtils.isEmpty(_dbUser) || StringUtils.isEmpty(_dbPw) || StringUtils.isEmpty(_dbName)) {
                _mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort));
            } else {
                _mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort), Collections.singletonList(MongoCredential.createCredential(_dbUser, _dbName, _dbPw.toCharArray())));
            }
        }

        if (_datastore == null)
            _datastore = morphia.createDatastore(_mongoClient, _dbName);

        return _datastore;
    }

    /**
     * @see IDatabaseDriver#getDatabase()
     */
    @Override
    public MongoDatabase getDatabase(){

        // I know there is a cleaner way todo this... but this works for now
        if (_database == null) {
            log.debug("init db interface");

            if (_mongoClient == null) {
                _mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort), Collections.singletonList(MongoCredential.createCredential(_dbUser, _dbName, _dbPw.toCharArray())));
            }

            _database = _mongoClient.getDatabase(_dbName);
        }

        return _database;
    }

    /**
     * @see IDatabaseDriver#getDB()
     */
    @Deprecated
    public DB getDB(){

        // I know there is a cleaner way todo this... but this works for now
        if (_db == null) {
            log.debug("init db interface");

            com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort));
            _mongoClient = mongoClient;

            _db = mongoClient.getDB(_dbName);
        }

        return _db;
    }
}
