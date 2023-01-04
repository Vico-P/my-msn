package com.mymsn.config.security.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mymsn.entities.Authority;
import com.mymsn.entities.User;
import com.mymsn.services.UserService;
import com.mymsn.utils.MyMsnUtils;

@Component("userDetailsService")
public class DomainUserService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public DomainUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    // Method used by Spring Security to retrieve a user in the DB
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        boolean isEmail = Pattern.compile(MyMsnUtils.REGEX_EMAIL).matcher(login).matches();
        if (isEmail) {
            return this.userService.findUserByEmail(login.toLowerCase())
                    .map(user -> new org.springframework.security.core.userdetails.User(user.getLogin(),
                            user.getPasswordHash(), Arrays.asList(new Authority("USER"))))
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email : " + login));
        }
        return this.userService.findUserByLogin(login.toLowerCase())
                .map(user -> new org.springframework.security.core.userdetails.User(user.getLogin(),
                        user.getPasswordHash(), Arrays.asList(new Authority("USER"))))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid login : " + login));
    }
}
