package org.example.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.example.socialmedia.model.User;
import org.example.socialmedia.repository.UserRepository;
import org.example.socialmedia.security.JwtService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String registerUser(String username, String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .build();

        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtService.generateToken(user);
    }

    public User getUserByID(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean isFollowing(Long id1, Long id2) {
        User user1 = userRepository.findById(id1).orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(id2).orElseThrow(() -> new RuntimeException("User not found"));

        return user1.getFollowing().contains(user2);
    }

    public void followOrUnfollow(Long id1, Long id2) {
        User user1 = userRepository.findById(id1).orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(id2).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user1.getFollowing().contains(user2)) {
            // FOLLOW:
            user1.getFollowing().add(user2);
            user2.getFollowers().add(user1);
        } else {
            // UNFOLLOW:
            user2.getFollowing().remove(user1);
            user1.getFollowers().remove(user2);
        }

        userRepository.save(user1);
        userRepository.save(user2);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
