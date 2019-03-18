package com.skeleton.project.engine;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

    private MongoDatabase database;

    public MongoDatabase getDatabase(){

        // I know there is a cleaner way todo this... but this works for now
        if (database == null) {
            log.debug("init db interface");

            MongoClient mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(_dbHost, _dbPort))))
                            .build());

            database = mongoClient.getDatabase(_dbName);
        }

        return database;
    }
}
