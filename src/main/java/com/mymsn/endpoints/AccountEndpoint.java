package com.mymsn.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mymsn.bodies.LoginBody;
import com.mymsn.config.jwt.TokenManager;
import com.mymsn.dtos.JwtDto;

@RestController
@RequestMapping("/api")
public class AccountEndpoint {
    private final TokenManager tokenManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AccountEndpoint(TokenManager tokenManager, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenManager = tokenManager;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    private ResponseEntity<JwtDto> loginAccount(@RequestBody LoginBody params) {
        // Creating a token to connect with Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                params.getLogin().toLowerCase(),
                params.getPassword());
        // Connecting with given credentials
        try {
            Authentication authentication = this.authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Setting succesfull connection in the context of this request
            // Generating new JWT token
            String token = tokenManager.generateJwtToken(params.getLogin());
            // Adding token to headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return new ResponseEntity<>(new JwtDto(token), headers, HttpStatus.OK);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password incorrect");

    }
}
