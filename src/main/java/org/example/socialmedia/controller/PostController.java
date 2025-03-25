package org.example.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmedia.dto.PostRequest;
import org.example.socialmedia.model.Post;
import org.example.socialmedia.model.User;
import org.example.socialmedia.repository.PostRepository;
import org.example.socialmedia.repository.UserRepository;
import org.example.socialmedia.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Post post = postService.createPost(user.getId(), postRequest.getContent());
        postRepository.save(post);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(Authentication authentication) {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId,  Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Post post = postService.likeOrUnlikePost(user.getId(), postId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", post.getLikedBy().contains(user));
        response.put("likes", post.getLikes());

        return ResponseEntity.ok(response);
    }
}
