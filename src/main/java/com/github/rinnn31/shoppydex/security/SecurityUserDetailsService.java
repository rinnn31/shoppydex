package com.github.rinnn31.shoppydex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.exception.UserNotFoundException;
import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return new SecurityUserDetail(user);
    }
    
}
