package com.skeleton.project.core;

import com.mongodb.DB;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
@Component
@PropertySource("classpath:application.properties") // Not necessary but leaving for explicit declaration sake
public class DatabaseDriver implements IDatabaseDriver{

    @Value("${db.host}")
    private String _dbHost;

    @Value("${db.port:27017}")
    private int _dbPort;

    @Value("${db.name:nexkey}")
    private String _dbName;

    private MongoDatabase _database;
    private com.mongodb.MongoClient _mongoClient;
    private Datastore _datastore;
    private final Morphia morphia = new Morphia();
    private DB _db;

    public MongoDatabase getDatabase(){

        // I know there is a cleaner way todo this... but this works for now
        if (_database == null) {
            log.debug("init db interface");

            MongoClient mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
//                                builder.hosts(Arrays.asList(new ServerAddress("mongodb://" + _dbHost, _dbPort))))
                                    builder.hosts(Arrays.asList(new ServerAddress(_dbHost, _dbPort))))
                            .build());

//            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
//                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//            MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry())

            _database = mongoClient.getDatabase(_dbName);
        }

        return _database;
    }

    /**
     * Returns old Deprecated MongoDb Object DB but MongoJack still requires it as far as I can tell.
     *
     * 4/15/19 although opting against using MongoJack (in favor of Morphia) leaving here @deprecated
     * in case it is ever useful.
     *
     * @return @DB
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

    public Datastore getDatastore() {
        if (_mongoClient == null) {
            _mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort));
        }

        if (_datastore == null)
            _datastore = morphia.createDatastore(_mongoClient, "nexkey");

        return _datastore;
    }
}
