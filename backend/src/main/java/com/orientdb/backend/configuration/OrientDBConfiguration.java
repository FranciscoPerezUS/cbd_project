package com.orientdb.backend.configuration;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrientDBConfiguration {

    @Value("${orientdb.url}")
    private String url;

    @Value("${orientdb.dbname}")
    private String dbName;

    @Value("${orientdb.username}")
    private String dbUsername;

    @Value("${orientdb.password}")
    private String dbPassword;

    @Bean(destroyMethod = "close")
    public OrientDB orientDB() {
        return new OrientDB(url, OrientDBConfig.defaultConfig());
    }

    @Bean
    public ODatabaseSession databaseSession(OrientDB orientDB) {
        return orientDB.open(dbName, dbUsername, dbPassword);
    }
}
