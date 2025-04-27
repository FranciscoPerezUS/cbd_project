package com.orientdb.backend.post;

import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {

    private final ODatabaseSession databaseSession;

    public PostRepository(ODatabaseSession databaseSession) {
        this.databaseSession = databaseSession;
    }

    public PostReply createPost(PostRequest post) throws Exception {
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
                postVertex.save();

                userResult.getVertex().get().addEdge(postVertex, "Made");

                PostReply createdPost = new PostReply();
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

    public List<PostReply> getAllPosts() {
        databaseSession.activateOnCurrentThread();

        List<PostReply> posts = new ArrayList<>();
        String query = "SELECT FROM Post";

        try (OResultSet rs = databaseSession.query(query)) {
            while (rs.hasNext()) {
                OResult result = rs.next();
                PostReply post = new PostReply();

                post.setTitle(result.getProperty("title"));
                post.setDescription(result.getProperty("description"));

                String edgeQuery = "";
                try (OResultSet edgeResultSet = databaseSession.query(edgeQuery, result.getIdentity())) {
                    if (edgeResultSet.hasNext()) {
                        OResult edgeResult = edgeResultSet.next();
                        post.setName(edgeResult.getProperty("name"));
                        post.setEmail(edgeResult.getProperty("email"));
                    }
                }

                posts.add(post);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch posts", e);
        }

        return posts;
    }
}
