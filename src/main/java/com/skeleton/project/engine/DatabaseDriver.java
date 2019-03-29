package com.skeleton.project.engine;

import com.mongodb.DB;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
@Repository
@PropertySource("classpath:application.properties") // Not necessary but leaving for explicit declaration sake
public class DatabaseDriver {

    @Value("${db.host:localhost}")
    private String _dbHost;

    @Value("${db.port:27017}")
    private int _dbPort;

    @Value("${db.name:nexkey}")
    private String _dbName;

    private MongoDatabase _database;
    private DB _db;

    public MongoDatabase getDatabase(){

        // I know there is a cleaner way todo this... but this works for now
        if (_database == null) {
            log.debug("init db interface");

            MongoClient mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(_dbHost, _dbPort))))
                            .build());

//            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
//                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

//            MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry())

            _database = mongoClient.getDatabase(_dbName);
        }

        return _database;
    }

    public DB getDB(){

        // I know there is a cleaner way todo this... but this works for now
        if (_db == null) {
            log.debug("init db interface");

            com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort));
//            MongoClient mongoClient = MongoClients.create(
//                    MongoClientSettings.builder()
//                            .applyToClusterSettings(builder ->
//                                builder.hosts(Arrays.asList(new ServerAddress(_dbHost, _dbPort))))
//                            .build());

//            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
//                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

//            MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry())

//            database = mongoClient.getDatabase(_dbName);
            _db = mongoClient.getDB(_dbName);
        }

        return _db;
    }
}
