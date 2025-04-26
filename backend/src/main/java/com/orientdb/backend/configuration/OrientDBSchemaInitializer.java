package com.orientdb.backend.configuration;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class OrientDBSchemaInitializer {

    private final ODatabaseSession dbSession;

    public OrientDBSchemaInitializer(ODatabaseSession dbSession) {
        this.dbSession = dbSession;
    }

    @PostConstruct
    public void initSchema() {
        OSchema schema = dbSession.getMetadata().getSchema();
        
        if (!schema.existsClass("User")) {
            OClass userClass = schema.createClass("User");
            userClass.createProperty("username", OType.STRING);
            userClass.createProperty("email", OType.STRING);
        }

        if (!schema.existsClass("User")) {
            OClass userClass = schema.createClass("User");
            userClass.createProperty("username", OType.STRING);
            userClass.createProperty("email", OType.STRING);
        }
    }
}
