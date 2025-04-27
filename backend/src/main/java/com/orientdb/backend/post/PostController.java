package com.orientdb.backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public ResponseEntity<PostReply> createPost(@RequestBody PostRequest postRequest) {
        try {
            PostReply createdPost = postRepository.createPost(postRequest);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@RequestBody LikeRequest likeRequest) {
        try {
            System.out.println("Received LikeRequest: " + likeRequest); // Log the request
            postRepository.likePost(likeRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PostReply>> getAllPosts() {
        try {
            List<PostReply> posts = postRepository.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
