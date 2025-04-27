package com.orientdb.backend.post;

import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class PostRepository {

    private final ODatabaseSession databaseSession;
    private static final Logger logger = Logger.getLogger(PostRepository.class.getName());

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
        String likesQuery = "SELECT in('LIKES').size() AS LikesNumber FROM Post WHERE @rid = ?";
        try (OResultSet rs = databaseSession.query(query)) {
            while (rs.hasNext()) {
                OResult result = rs.next();
                PostReply post = new PostReply();

                post.setId(result.getIdentity().get().toString());
                post.setTitle(result.getProperty("title"));
                post.setDescription(result.getProperty("description"));
                OVertex userVertex = result.getVertex().get().getEdges(ODirection.IN, "Made")
                                               .iterator()
                                               .next()
                                               .getVertex(ODirection.OUT);
                try (OResultSet rs2 = databaseSession.query(likesQuery,post.getId())) {
                    post.setLikes(rs2.next().getProperty("LikesNumber"));
                }
                
                post.setName(userVertex.getProperty("name"));
                post.setEmail(userVertex.getProperty("email"));
                posts.add(post);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch posts", e);
        }

        return posts;
    }

    public void likePost(LikeRequest likeRequest) throws Exception {
        databaseSession.activateOnCurrentThread();

        String userQuery = "SELECT FROM User WHERE username = ? AND password = ?";
        String postQuery = "SELECT FROM Post WHERE @rid = ?";

        System.out.println(likeRequest.getPostId());

        try (OResultSet userResultSet = databaseSession.query(userQuery, likeRequest.getUsername(), likeRequest.getPassword());
             OResultSet postResultSet = databaseSession.query(postQuery, likeRequest.getPostId())) {

            if (userResultSet.hasNext() && postResultSet.hasNext()) {
                OVertex userVertex = userResultSet.next().getVertex().get();
                OVertex postVertex = postResultSet.next().getVertex().get();

                OEdge likesPost = userVertex.addEdge(postVertex, "Likes");
                likesPost.save();
            } else {
                logger.warning("User or Post not found.");
                throw new Exception("User or Post not found.");
            }
        } catch (Exception e) {
            logger.severe("Error in likePost: " + e.getMessage());
            throw e;
        }
    }
}
