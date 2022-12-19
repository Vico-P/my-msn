package com.mymsn.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // TODO Setup Spring security filter chain
    // TODO SpringSecurity seems to return 403 error instead of 401 need to check if
    // cors or csrf problems
    http.authorizeHttpRequests(
        (request) -> request.requestMatchers("/api/users/**").permitAll().anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}