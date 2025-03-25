package org.example.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.example.socialmedia.model.Post;
import org.example.socialmedia.model.User;
import org.example.socialmedia.repository.PostRepository;
import org.example.socialmedia.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    public final PostRepository postRepository;
    public final UserRepository userRepository;

    public Post createPost(Long userId, String content) {
        User author = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void likeOrUnlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Set<Post> likedPosts = user.getLikedPosts();
        Set<User> likedBy = post.getLikedBy();

        if (likedPosts.contains(post)) {
            // If user already liked -> Unlike
            likedPosts.remove(post);
            likedBy.remove(post);
            post.setLikes(post.getLikes() - 1);
        } else {
            // If user has not liked -> Like
            likedPosts.add(post);
            likedBy.add(user);
            post.setLikes(post.getLikes() + 1);
        }

        userRepository.save(user);
        postRepository.save(post);

    }

}
