package org.example.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmedia.dto.PostRequest;
import org.example.socialmedia.model.Post;
import org.example.socialmedia.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal org.example.socialmedia.model.User user) {
        Post post = postService.createPost(user.getId(), postRequest.getContent());
        return ResponseEntity.ok().body(post);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@AuthenticationPrincipal org.example.socialmedia.model.User user) {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal org.example.socialmedia.model.User user) {
        postService.likeOrUnlikePost(user.getId(), postId);
        return ResponseEntity.ok().build();
    }
}
