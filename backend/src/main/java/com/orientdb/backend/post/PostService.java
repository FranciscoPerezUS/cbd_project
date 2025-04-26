package com.orientdb.backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post, Integer userId) {
        postRepository.createPerson(post, userId);
        return post;
    }

    public Post getPostByTitle(String title) {
        return postRepository.findById(title);
    }
}
