package com.mymsn.config.security.utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mymsn.entities.Authority;
import com.mymsn.entities.Log;
import com.mymsn.entities.User;
import com.mymsn.exception.HttpErrorException;
import com.mymsn.services.LogService;
import com.mymsn.services.UserService;
import com.mymsn.utils.MyMsnUtils;
import com.mymsn.utils.enums.Action;

@Component("userDetailsService")
public class DomainUserService implements UserDetailsService {
    private final UserService userService;

    private final LogService logService;

    @Autowired
    public DomainUserService(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    // Method used by Spring Security to retrieve a user in the DB
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        boolean isEmail = Pattern.compile(MyMsnUtils.REGEX_EMAIL).matcher(login).matches();
        if (isEmail) {
            return this.userService.findUserByEmail(login.toLowerCase())
                    .map(user -> {
                        if (!user.isIsVerified()) {
                            this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now())
                                    .message("Login error : account with login \"" + user.getLogin()
                                            + "\" is not verified"));
                            throw new HttpErrorException(HttpStatus.UNAUTHORIZED, "Login error",
                                    "Your account is not verified");
                        }
                        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                                user.getPasswordHash(), Arrays.asList(new Authority("USER")));
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email : " + login));
        }
        return this.userService.findUserByLogin(login.toLowerCase())
                .map(user -> {
                    if (!user.isIsVerified()) {
                        this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now()).message(
                                "Login error : account with login \"" + user.getLogin() + "\" is not verified"));
                        throw new HttpErrorException(HttpStatus.UNAUTHORIZED, "Login error",
                                "Your account is not verified");
                    }
                    return new org.springframework.security.core.userdetails.User(user.getLogin(),
                            user.getPasswordHash(), Arrays.asList(new Authority("USER")));
                })
                .orElseThrow(() -> new UsernameNotFoundException("Invalid login : " + login));
    }
}
