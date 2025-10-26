package com.github.rinnn31.shoppydex.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.model.api.AuthenticationDTO;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.security.JwtTokenService;
import com.github.rinnn31.shoppydex.security.SPDPasswordEncoder;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private SPDPasswordEncoder passwordEncoder;

    public AuthenticationDTO authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtTokenService.generateToken(user.getUsername());
            long expirationTime = jwtTokenService.extractExpiration(token).getTime();
            return new AuthenticationDTO(token, user.getUsername(), expirationTime);
        } else {
            return null;
        }
    }

    public void register(String username, String email, String password) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new SPDException(100, "Tên đăng nhập hoặc email đã được sử dụng");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, email, encodedPassword);
        newUser.setRole(User.ROLE_USER);
        userRepository.save(newUser);
    }
}
