package com.orientdb.backend;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude = { 
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class 
})
public class BackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

    private final ODatabaseSession databaseSession;

    public BackendApplication(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @PostConstruct
    public void initializeDatabase() {
        try {
            OSchema schema = databaseSession.getMetadata().getSchema();

            logger.info("Initializing database schema...");
            logger.info("Existing classes: {}", schema.getClasses());

            if (!schema.existsClass("User")) {
                logger.info("Creating vertex class 'User'...");
                schema.createClass("User", schema.getClass("V"));
            }
            if (!schema.existsClass("Post")) {
                logger.info("Creating vertex class 'Post'...");
                schema.createClass("Post", schema.getClass("V"));
            }
            if (!schema.existsClass("Made")) {
                logger.info("Creating edge class 'Made'...");
                schema.createClass("Made", schema.getClass("E"));
            }
            if (!schema.existsClass("Likes")) {
                logger.info("Creating edge class 'Likes'...");
                schema.createClass("Likes", schema.getClass("E"));
            }

            logger.info("Database schema initialization completed.");
        } catch (Exception e) {
            logger.error("Error during database schema initialization", e);
        }
    }
}
