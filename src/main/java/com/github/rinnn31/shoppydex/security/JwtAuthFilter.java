package com.github.rinnn31.shoppydex.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTojenService;

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = parseJwtToken(request);
        if (token == null || !StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final String username = jwtTojenService.validateTokenAndGetUsername(token);
            UserDetails userDetails = null;
            if (username != null &&
                (userDetails = userDetailsService.loadUserByUsername(username)) != null &&
                token.equals(((SecurityUserDetail)userDetails).getActualActiveToken())) {

               UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwtToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
