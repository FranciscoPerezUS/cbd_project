package com.orientdb.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.createUser(user);
    }

    @GetMapping("/{name}")
    public User getUserByName(@PathVariable String name) {
        return new User();
    }
}
