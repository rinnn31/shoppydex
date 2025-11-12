package com.github.rinnn31.shoppydex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.SPDException;
import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.dto.UserInfoModel;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserInfoModel getUserInfo(String username) {
        UserEntity user = getUser(username);
        return new UserInfoModel(user);
    }   

    public void updatePassword(String username, String oldPassword, String newPassword) {
        UserEntity user = getUser(username);
        if (!passwordEncoder.matches(oldPassword,  user.getPassword())) {
            throw new SPDException(101, "Mật khẩu cũ không đúng");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setUserPoints(String username, Integer points) {
        UserEntity user = getUser(username);
        user.setPoints(points);
        userRepository.save(user);
    }

    public void addUserPoints(String username, Integer pointsToAdd) {
        UserEntity user = getUser(username);
        user.setPoints(user.getPoints() + pointsToAdd);
        userRepository.save(user);
    }

    public void changeEmail(String username, String newEmail, String password) {
        UserEntity user = getUser(username);
        if (!passwordEncoder.matches(password,  user.getPassword())) {
            throw new SPDException(101, "Mật khẩu không đúng");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
    }
}
