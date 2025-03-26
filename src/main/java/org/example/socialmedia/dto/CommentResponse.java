package org.example.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.socialmedia.model.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private int likes;
    private boolean liked;
    private Long postId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;

    /**
     * Creates a new CommentResponse from a Comment Entity
     * Intended for use in HTTP requests
     * @param comment
     * @param currentUserId
     * @return New CommentResponse
     */
    public static CommentResponse fromEntity(Comment comment, Long currentUserId) {
        boolean liked = comment.getLikedBy().stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                liked,
                comment.getPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        );
    }

}
