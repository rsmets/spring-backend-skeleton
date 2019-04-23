package com.skeleton.project.core;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
@Component
@PropertySource("classpath:application.properties") // Not necessary but leaving for explicit declaration sake
public class DatabaseDriver implements IDatabaseDriver{

//    @Value("${db.host}")
//    private String _dbHost = "nk-sandbox:thepassword1015@ds241369-a0.mlab.com:41369,ds241369-a1.mlab.com:41369/nexkey-sandbox?replicaSet=rs-ds241369";
//    private String _dbHost = "mongodb://nk-sandbox:thepassword1015@ds241369-a0.mlab.com:41369/nexkey-sandbox";
//    private String _dbHost = "ds241369-a0.mlab.com";

    @Value("${db.host}")
    private String _dbHost;

    @Value("${db.port}")
    private int _dbPort;
//    private int _dbPort = 41369;

    @Value("${db.name}")
    private String _dbName;
//    private String _dbName = "nexkey-sandbox";

    @Value("${db.user}")
    private String _dbUser;

    @Value("${db.pw}")
    private String _dbPw;

    private MongoDatabase _database;
    private com.mongodb.MongoClient _mongoClient;
    private Datastore _datastore;
    private final Morphia morphia = new Morphia();
    private DB _db;

    public MongoDatabase getDatabase(){

        // I know there is a cleaner way todo this... but this works for now
        MongoClientURI uri = new MongoClientURI(_dbHost);
//        MongoClientURI uri = new MongoClientURI("mongodb://" + _dbHost + ":" + _dbPort);

        if (_database == null) {
            log.debug("init db interface");

//            MongoClient mongoClient = MongoClients.create(
//                    MongoClientSettings.builder()
//                            .applyToClusterSettings(builder ->
////                                builder.hosts(Arrays.asList(new ServerAddress("mongodb://" + _dbHost, _dbPort))))
//                                    builder.hosts(Arrays.asList(new ServerAddress(_dbHost, _dbPort))))
//                            .build());

//            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
//                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//            MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry())

            MongoClient m = new MongoClient(uri);
            _database = m.getDatabase(uri.getDatabase());
//            _database = m.getDatabase(_dbName);
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
            _mongoClient = new com.mongodb.MongoClient(new ServerAddress(_dbHost, _dbPort), Collections.singletonList(MongoCredential.createCredential(_dbUser, _dbName, _dbPw.toCharArray())));
        }

        if (_datastore == null)
            _datastore = morphia.createDatastore(_mongoClient, _dbName);

        return _datastore;
    }
}
