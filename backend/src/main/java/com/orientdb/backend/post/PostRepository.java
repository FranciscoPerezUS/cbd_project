package com.orientdb.backend.post;

import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

@Repository
public class PostRepository {

    private final ODatabaseSession databaseSession;

    public PostRepository(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;
    }

    public Post createPost(PostRequest post) throws Exception {
        databaseSession.activateOnCurrentThread();
        String statement = "SELECT FROM User WHERE username = ? AND password = ?";

        try (OResultSet rs = databaseSession.query(statement, post.getUsername(), post.getPassword())) {
            if (rs.hasNext()) {
                var userResult = rs.next();
                String name = userResult.getProperty("name");
                String email = userResult.getProperty("email");

                OVertex postVertex = databaseSession.newVertex("Post");
                postVertex.setProperty("title", post.getTitle());
                postVertex.setProperty("description", post.getDescription());
                postVertex.setProperty("name", name);
                postVertex.setProperty("email", email);
                postVertex.save();

                userResult.getVertex().get().addEdge(postVertex, "Made");

                Post createdPost = new Post();
                createdPost.setTitle(post.getTitle());
                createdPost.setDescription(post.getDescription());
                createdPost.setName(name);
                createdPost.setEmail(email);
                return createdPost;

            } else {
                throw new Exception("User with credentials given not found.");
            }
        }
    }   
}
