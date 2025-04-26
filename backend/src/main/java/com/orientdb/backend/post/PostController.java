package com.orientdb.backend.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private ODatabaseSession databaseSession;

    @PostMapping
    public Post createPost(@RequestBody Post post, @RequestParam Integer userId) {
        return null;
    }

    @GetMapping("/{title}")
    public Post getPostByTitle(@PathVariable String title) {
        return null;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        databaseSession.activateOnCurrentThread(); // Ensure the session is active

        List<Post> posts = new ArrayList<>();
        String query = "SELECT FROM Post";

        try (OResultSet rs = databaseSession.query(query)) { // Use the session's query method
            while (rs.hasNext()) {
                OResult result = rs.next();
                Post post = new Post();

                post.setTitle(result.getProperty("title"));
                post.setDescription(result.getProperty("description"));
                posts.add(post);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch posts", e);
        }

        return posts;
    }
}
