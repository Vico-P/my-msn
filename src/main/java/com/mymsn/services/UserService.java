package com.mymsn.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mymsn.bodies.RegisterBody;
import com.mymsn.entities.Log;
import com.mymsn.entities.User;
import com.mymsn.exception.HttpErrorException;
import com.mymsn.repository.UserRepository;
import com.mymsn.utils.MyMsnUtils;
import com.mymsn.utils.enums.Action;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final LogService logService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailService mailService,
            LogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.logService = logService;
    }

    public Optional<User> findUserById(String id) {
        return this.userRepository.findById(UUID.fromString(id));
    }

    public Optional<User> findUserByLogin(String login) {
        return this.userRepository.findByLoginIgnoreCase(login.toLowerCase());
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmailIgnoreCase(email.toLowerCase());
    }

    public void verifyUserEmail(String token) {
        String[] decryptedToken = MyMsnUtils.decrypt(token).split(":");
        if (decryptedToken.length == 1) {
            this.logService.saveLog(new Log().action(Action.ERROR).message(
                    "Email verify error : The given token \"" + token + "\" is not valid ")
                    .createdAt(LocalDateTime.now()));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Email verify error", "The given token is not valid");
        }
        String emailUser = decryptedToken[0];
        String idUser = decryptedToken[1];
        Optional<User> optionalUser = this.findUserById(idUser);
        if (optionalUser.isPresent() && optionalUser.get().getEmail().equalsIgnoreCase(emailUser)) {
            optionalUser.get().setUpdatedAt(LocalDateTime.now());
            optionalUser.get().setIsVerified(true);
            this.logService.saveLog(new Log().action(Action.UPDATE).createdAt(LocalDateTime.now())
                    .message("Activating user \"" + idUser + "\", email verified"));
        } else if (optionalUser.isPresent() && !optionalUser.get().getEmail().equalsIgnoreCase(emailUser)) {
            this.logService.saveLog(new Log().action(Action.ERROR).message(
                    "Email verify error : Email provided is not the same as the email of the user matching the id \""
                            + idUser + "\"")
                    .createdAt(LocalDateTime.now()));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Email verify error",
                    "Email provided is not the same as the email of the user matching of the id provided");
        } else {
            this.logService.saveLog(new Log().action(Action.ERROR).message(
                    "Email verify error : No user with id \"" + idUser + "\n")
                    .createdAt(LocalDateTime.now()));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Email verify error", "No user with id provided");
        }
    }

    public User createOrUpdateNonVerifiedUser(RegisterBody params) {
        Optional<User> resultQueryEmail = this.findUserByEmail(params.getEmail());
        Optional<User> resultQueryLogin = this.findUserByLogin(params.getUsername());
        // If email is present on one user and the login is present on another user, we
        // throw an error because we risk to have two accounts with same login
        if (resultQueryEmail.isPresent() && resultQueryLogin.isPresent()
                && !resultQueryEmail.get().getId().equals(resultQueryLogin.get().getId())) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Register error", "Email and username already took");
        }
        // If email is present and user is verified, we throw an error because email is
        // been use by someone
        else if ((resultQueryEmail.isPresent() && resultQueryEmail.get().isIsVerified())) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Register error", "Email is already took");
        }
        // If login is present and user is verified, we throw an error because login is
        // been use by someone
        else if (resultQueryLogin.isPresent() && resultQueryLogin.get().isIsVerified()) {
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Register error", "Username is already took");
        }
        // We set User object to update with the result from our queries
        User toCreateOrUpdate = resultQueryEmail.isPresent() ? resultQueryEmail.get() : null;
        toCreateOrUpdate = toCreateOrUpdate != null && resultQueryLogin.isPresent() ? resultQueryLogin.get()
                : toCreateOrUpdate;
        // If both of our queries returned nothing we create a new User
        toCreateOrUpdate = toCreateOrUpdate == null ? new User() : toCreateOrUpdate;
        // Saving user's informations
        toCreateOrUpdate = this.userRepository
                .save(toCreateOrUpdate.login(params.getUsername()).email(params.getEmail())
                        .passwordHash(this.passwordEncoder.encode(params.getPassword()))
                        .createdAt(toCreateOrUpdate.getCreatedAt() == null ? LocalDateTime.now()
                                : toCreateOrUpdate.getCreatedAt())
                        .isVerified(false));
        try {
            // Sending email to verify email
            this.mailService.sendVerifyEmail(toCreateOrUpdate);
            this.logService
                    .saveLog(new Log()
                            .action(Action.CREATE).message("Creating new account : email = "
                                    + toCreateOrUpdate.getEmail() + ", username = " + toCreateOrUpdate.getLogin())
                            .createdAt(LocalDateTime.now()));
        } catch (MessagingException ex) {
            this.logService
                    .saveLog(new Log()
                            .action(Action.ERROR).message("Error while creating new account : email = "
                                    + toCreateOrUpdate.getEmail() + ", username = " + toCreateOrUpdate.getLogin()
                                    + " cause => " + ex.getMessage())
                            .createdAt(LocalDateTime.now()));
            log.error("An error occured while sending a message to \"" + toCreateOrUpdate.getEmail() + "\"", ex);
        }
        return toCreateOrUpdate;
    }
}
