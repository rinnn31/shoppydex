package com.github.rinnn31.shoppydex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository accountRepository;

    public boolean isUsernameTaken(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return accountRepository.existsByEmail(email);
    }

    public User getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public User getAccountByUserOrEmail(String identifier) {
        return accountRepository.findByUsernameOrEmail(identifier, identifier);
    }

    public List<String> getAllUsernames() {
        return accountRepository.findAll()
                .stream()
                .map(account -> account.getUsername())
                .toList();
    }

    public User createAccount(String username, String email, String password) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        User account = new User(username, email, password);
        account.setRole(User.ROLE_USER);
        return accountRepository.save(account);
    }

    public User updateAccount(User account) {
        return accountRepository.save(account);
    }

    public boolean deleteAccountByUsername(String username) {
        User account = accountRepository.findByUsername(username);
        if (account != null) {
            accountRepository.delete(account);
            return true;
        }
        return false;
    }
}
