package org.example.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.socialmedia.dto.UserResponse;
import org.example.socialmedia.model.User;
import org.example.socialmedia.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id, Authentication auth) {
        User user = userService.getUserByID(id);
        User currentUser = userService.findByUsername(auth.getName());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getFollowers().size(),
                user.getFollowing().size(),
                user.getPosts(),
                currentUser != null && userService.isFollowing(currentUser.getId(), user.getId())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<?> followOrUnfollow(@PathVariable Long targetUserId, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        userService.followOrUnfollow(targetUserId, user.getId());
        return ResponseEntity.ok().build();
    }

//    @PatchMapping(value = "/{id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<UserResponse> updateProfile(
//            @PathVariable Long id,
//            @RequestParam(required = false) String bio,
//            @RequestParam(required = false) MultipartFile profilePicture,
//            @AuthenticationPrincipal User currentUser)
//    throws IOException {
//        if (!currentUser.getId().equals(id)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        User updatedUser = userService.updateProfile(id, bio, profilePicture);
//
//        UserResponse response = new UserResponse(
//                updatedUser.getId(),
//                updatedUser.getUsername(),
//                updatedUser.getBio(),
//                updatedUser.getProfilePictureUrl(),
//                updatedUser.getFollowers().size(),
//                updatedUser.getFollowing().size(),
//                false // can't follow yourself
//        );
//
//        return ResponseEntity.ok(response);
//    }

}
