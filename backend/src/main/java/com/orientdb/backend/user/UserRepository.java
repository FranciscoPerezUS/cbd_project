package com.orientdb.backend.user;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final ODatabaseSession databaseSession;

    public UserRepository(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;
    }

    public User createUser(User user) {
        databaseSession.activateOnCurrentThread();

        OVertex result = databaseSession.newVertex("User");

        result.setProperty("name", user.getName());
        result.setProperty("email", user.getEmail());
        result.setProperty("password", user.getPassword());
        result.setProperty("username", user.getUsername());
        result.save();
        
        return user;
    }
}
