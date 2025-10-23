package com.github.rinnn31.shoppydex.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SPDPasswordEncoder implements PasswordEncoder {
    private static final String TAG = "SPD:";

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            String finalPass = TAG + rawPassword.toString();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(finalPass.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error encoding password", ex);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
    
}
