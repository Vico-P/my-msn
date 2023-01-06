package com.mymsn.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mymsn.config.jwt.TokenManager;
import com.mymsn.config.security.utils.JwtFilter;
import com.mymsn.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  private final TokenManager tokenManager;

  private final UserRepository userRepository;

  @Autowired
  public SecurityConfiguration(TokenManager tokenManager, UserRepository userRepository) {
    this.tokenManager = tokenManager;
    this.userRepository = userRepository;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeHttpRequests(
        (request) -> request
            .requestMatchers("/api/login").permitAll()
            .requestMatchers("/api/register").permitAll()
            .requestMatchers("/api/verify-email").permitAll()
            .requestMatchers("/api/reset-password/**").permitAll()
            .anyRequest().authenticated())
        // Adding our filter to decode the JWTToken inside the header
        .addFilterBefore(new JwtFilter(this.tokenManager, this.userRepository),
            UsernamePasswordAuthenticationFilter.class)
        .httpBasic();

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}