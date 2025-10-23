package com.github.rinnn31.shoppydex.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenService {
    private static final String SECRET_KEY = "vYxgEPhlOvs7Hq_8pT3cBqNfG7eS0oZqXcYlLhWqDjpKJd5e";

    private static SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7_200_000))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            String username = extractUsername(token);
            Date expiration = extractExpiration(token);
            if (expiration.after(new Date())) {
                return username;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
