package com.github.rinnn31.shoppydex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.model.User;
import com.github.rinnn31.shoppydex.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = accountRepository.findByUsername(username);
        if(account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new SecurityUserDetail(account);
    }
    
}
