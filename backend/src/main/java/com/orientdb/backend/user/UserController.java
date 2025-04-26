package com.orientdb.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.OVertexDocument;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend's URL
public class UserController {

    @Autowired
    private ODatabaseSession db;

    private static User createUser(ODatabaseSession db, User user) {
        db.activateOnCurrentThread(); 
        OVertex result = db.newVertex("User");
        result.setProperty("name", user.getName());
        result.setProperty("email", user.getEmail());
        result.save();
        return user;
      }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return createUser(db, user);
    }

    @GetMapping("/{name}")
    public User getUserByName(@PathVariable String name) {
        return new User();
    }
}
