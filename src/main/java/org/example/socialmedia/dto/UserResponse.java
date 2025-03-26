package org.example.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.socialmedia.model.Post;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String bio;
    private String profilePictureUrl;
    private int followerCount;
    private int followingCount;
    private List<Post> posts;
    private boolean isFollowedByCurrentUser;
}
