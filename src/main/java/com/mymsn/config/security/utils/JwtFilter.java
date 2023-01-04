package com.mymsn.config.security.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mymsn.config.jwt.TokenManager;
import com.mymsn.entities.Authority;
import com.mymsn.entities.User;
import com.mymsn.repository.UserRepository;
import com.mymsn.utils.MyMsnUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    public JwtFilter(TokenManager tokenManager, UserRepository userRepository) {
        this.tokenManager = tokenManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get token
        String authorizationContent = request.getHeader("Authorization");
        if (authorizationContent != null && authorizationContent.indexOf("Bearer ") == 0) {
            String token = authorizationContent.replace("Bearer ", "").trim();
            try {
                Jws<Claims> identity = this.tokenManager.parseJwt(token);
                String login = identity.getBody().get("login").toString();
                // Check if the login is an email or a username
                Optional<User> optionalUser = Pattern.compile(MyMsnUtils.REGEX_EMAIL).matcher(login).matches()
                        ? this.userRepository.findByEmailIgnoreCase(login)
                        : this.userRepository.findByLoginIgnoreCase(login);
                if (optionalUser.isPresent()) {
                    // Setting user identity in security context
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            optionalUser.get().getLogin().toLowerCase(), token,
                            Arrays.asList(new Authority("USER")));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.error("Error : User with username/email  " + login + " not found");
                }
            } catch (SignatureException | ExpiredJwtException ex) {
                log.error("Error while trying to decode token", ex);
            }
        }
        filterChain.doFilter(request, response);
    }
}
