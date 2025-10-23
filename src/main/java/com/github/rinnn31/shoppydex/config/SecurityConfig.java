package com.github.rinnn31.shoppydex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.github.rinnn31.shoppydex.model.User.ROLE_ADMIN;
import com.github.rinnn31.shoppydex.security.JwtAuthFilter;
import com.github.rinnn31.shoppydex.security.SPDAccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SPDAccessHandler accessDeniedHandler;
    
    @Autowired
    private JwtAuthFilter jwtAuthFilter;    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                       // .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/admin/**").hasRole(ROLE_ADMIN)
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(accessDeniedHandler)
                )
                .sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
