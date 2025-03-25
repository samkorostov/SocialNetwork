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
}
