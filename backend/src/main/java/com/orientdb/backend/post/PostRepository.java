package com.orientdb.backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

@Repository
public class PostRepository {

    @Autowired
    private ODatabaseSession db;

    public OVertex createPerson(Post post, Integer userId) {
        OVertex result = db.newVertex("Post");
        result.setProperty("title", post.getTitle());
        result.setProperty("description", post.getDescription());
        result.save();
        return result;
    }

    public Post findById(String title) {
        String query = "SELECT FROM User WHERE title CONTAINS ?";
        try (OResultSet rs = db.query(query, title)) {
            if (rs.hasNext()) {
                OResult r = rs.next();
                Post p = new Post();
                p.setTitle(r.getProperty("title"));
                p.setDescription(r.getProperty("description"));
                return p;
            }
        }
        return null;
    }
}
