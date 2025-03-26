package org.example.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmedia.dto.PostRequest;
import org.example.socialmedia.dto.PostResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.socialmedia.dto.PostResponse.fromEntity;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException(authentication.getName())
        );
        Post post = postService.createPost(user.getId(), postRequest.getContent());
        postRepository.save(post);
        PostResponse postResponse = fromEntity(post, user.getId());

        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(Authentication authentication) {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();

        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException(authentication.getName())
        );
        for (Post post : posts) {
            postResponses.add(fromEntity(post, currentUser.getId()));
        }

        return ResponseEntity.ok(postResponses);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId,  Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException(authentication.getName())
        );
        Post post = postService.likeOrUnlikePost(user.getId(), postId);

        PostResponse postResponse = fromEntity(post, user.getId());
        return ResponseEntity.ok(postResponse);
    }
}
