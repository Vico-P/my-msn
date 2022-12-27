package com.mymsn.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mymsn.config.jwt.TokenManager;
import com.mymsn.config.security.utils.JwtFilter;
import com.mymsn.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  private final TokenManager tokenManager;

  private final UserService userService;

  @Autowired
  public SecurityConfiguration(TokenManager tokenManager, UserService userService) {
    this.tokenManager = tokenManager;
    this.userService = userService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeHttpRequests(
        (request) -> request.requestMatchers("/api/login").permitAll().anyRequest().authenticated())
        // Adding our filter to decode the JWTToken inside the header
        .addFilterBefore(new JwtFilter(this.tokenManager, this.userService),
            UsernamePasswordAuthenticationFilter.class)
        // This is to tell Spring to send Http 401 Unauthorized error when we try to
        // make request without being authenticated
        // Or else Spring will send by default Http 403 Forbidden error
        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}