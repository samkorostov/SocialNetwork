package org.example.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.socialmedia.model.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private int likes;
    private boolean liked;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;

    /**
     * Creates a post response from a Post entity
     * @param post
     * @param currentUserId
     * @return new CommentResponse
     */
    public static PostResponse fromEntity(Post post, Long currentUserId) {
        boolean likedByCurrentUser = post.getLikedBy().stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getLikes(),
                likedByCurrentUser,
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt()
        );
    }
}
