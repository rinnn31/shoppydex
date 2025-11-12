package com.github.rinnn31.shoppydex.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.model.enums.UserRole;
import com.github.rinnn31.shoppydex.repository.UserRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        initializeWorkingDirectory();   
        createDefaultAdminUser();
    }

    @Transactional
    private void createDefaultAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(UserRole.ADMIN);
            adminUser.setPoints(1000000);
            adminUser.setEmail("damquangphongthdk@gmail.com");
            userRepository.save(adminUser);
        }
    }

    private void initializeWorkingDirectory() {
        Path databaseDir = Paths.get("data/database");
        if (!Files.exists(databaseDir) || !Files.isDirectory(databaseDir)) {
            databaseDir.toFile().mkdirs();
        }

        Path productsDir = Paths.get("data/products");
        if (!Files.exists(productsDir) || !Files.isDirectory(productsDir)) {
            productsDir.toFile().mkdirs();
        }

        Path uploadsDir = Paths.get("uploads");
        if (!Files.exists(uploadsDir) || !Files.isDirectory(uploadsDir)) {
            uploadsDir.toFile().mkdirs();
        }
    }
    
}
