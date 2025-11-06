package com.github.rinnn31.shoppydex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.repository.UserRepository;
import com.github.rinnn31.shoppydex.security.SPDPasswordEncoder;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SPDPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        System.out.println(userRepository.count() + " users found in database.");
        userRepository.deleteByRole(UserEntity.ROLE_ADMIN);
        userRepository.flush();

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(UserEntity.ROLE_ADMIN);
        adminUser.setEmail("damquangphongthdk@gmail.com");
        userRepository.save(adminUser);
    }
    
}
