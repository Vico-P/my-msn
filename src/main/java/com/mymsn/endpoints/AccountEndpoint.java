package com.mymsn.endpoints;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mymsn.bodies.LoginBody;
import com.mymsn.bodies.RegisterBody;
import com.mymsn.config.jwt.TokenManager;
import com.mymsn.dtos.JwtDto;
import com.mymsn.dtos.UserDto;
import com.mymsn.entities.Log;
import com.mymsn.entities.User;
import com.mymsn.exception.HttpErrorException;
import com.mymsn.services.LogService;
import com.mymsn.services.MailService;
import com.mymsn.services.UserService;
import com.mymsn.utils.MyMsnUtils;
import com.mymsn.utils.enums.Action;

@RestController
@RequestMapping("/api")
public class AccountEndpoint {
    private final Logger log = LoggerFactory.getLogger(AccountEndpoint.class);

    private final TokenManager tokenManager;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    private final LogService logService;

    @Autowired
    public AccountEndpoint(TokenManager tokenManager, AuthenticationManagerBuilder authenticationManagerBuilder,
            UserService userService, MailService mailService, LogService logService) {
        this.tokenManager = tokenManager;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.logService = logService;
    }

    @PatchMapping("/verify-email")
    private ResponseEntity<Void> validateAccount(@RequestParam String token) {
        if (token == null) {
            log.error("No token provided, cannot verify email");
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Email verify error",
                    "Missing token parameter in URL");
        }
        this.userService.verifyUserEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    private ResponseEntity<UserDto> registerAccount(@RequestBody RegisterBody params) {
        if (!params.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form invalid");
        }
        return ResponseEntity.ok().body(new UserDto(this.userService.createOrUpdateNonVerifiedUser(params)));
    }

    @PostMapping("/login")
    private ResponseEntity<JwtDto> loginAccount(@RequestBody LoginBody params) {
        // Creating a token to connect with Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                params.getLogin(),
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
            this.logService.saveLog(
                    new Log().action(Action.CONNECT)
                            .message("Connect with success for login \"" + params.getLogin() + "\"")
                            .createdAt(LocalDateTime.now()));
            return new ResponseEntity<>(new JwtDto(token), headers, HttpStatus.OK);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            this.logService.saveLog(
                    new Log().action(Action.ERROR)
                            .message("Error while connection with login \"" + params.getLogin() + "\"")
                            .createdAt(LocalDateTime.now()));
        }
        throw new HttpErrorException(HttpStatus.UNAUTHORIZED, "Login failed", "Username or password incorrect");
    }
}
