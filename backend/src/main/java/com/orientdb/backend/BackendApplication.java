package com.orientdb.backend;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
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

            logger.info("Dropping all vertices and edges...");
            databaseSession.command("DELETE VERTEX V UNSAFE").close();
            databaseSession.command("DELETE EDGE E UNSAFE").close();
            logger.info("All vertices and edges dropped.");

            logger.info("Initializing database schema...");

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

            logger.info("Adding initial data...");

            // Create initial users with roles
            OVertex adminUser = databaseSession.newVertex("User");
            adminUser.setProperty("name", "Admin");
            adminUser.setProperty("email", "admin@example.com");
			adminUser.setProperty("username", "admin");
			adminUser.setProperty("password", "admin123");
            adminUser.save();

            OVertex regularUser = databaseSession.newVertex("User");
            regularUser.setProperty("name", "User");
            regularUser.setProperty("email", "user@example.com");
			regularUser.setProperty("username", "user");
			regularUser.setProperty("password", "user123");
            regularUser.save();

			OVertex postAdmin = databaseSession.newVertex("Post");
            postAdmin.setProperty("title", "Publicación del admin");
            postAdmin.setProperty("description", "Esto es una publicación del admin");
            postAdmin.save();

            OVertex postUser = databaseSession.newVertex("Post");
            postUser.setProperty("title", "Publicación de un usuario");
            postUser.setProperty("description", "Esto es una publicación de un usuario cualquiera");
            postUser.save();

			OEdge madeByAdmin = adminUser.addEdge(postAdmin, "Made");
			madeByAdmin.save();

			OEdge madeByUser = regularUser.addEdge(postUser, "Made");
			madeByUser.save(); 
			

            logger.info("Initial data added successfully.");
        } catch (Exception e) {
            logger.error("Error during database schema initialization", e);
        }
    }
}
