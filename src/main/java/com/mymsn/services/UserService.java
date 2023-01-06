package com.mymsn.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import com.mymsn.properties.ConfigurationAppProperties;
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

    private final ConfigurationAppProperties configurationAppProperties;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailService mailService,
            LogService logService, ConfigurationAppProperties configurationAppProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.logService = logService;
        this.configurationAppProperties = configurationAppProperties;
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
        String[] decryptedToken = MyMsnUtils.decrypt(token).split(MyMsnUtils.TOKEN_SEPERATOR);
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

    public void finishResetPassword(String password, String token) {
        String[] decryptedToken = MyMsnUtils.decrypt(token).split(MyMsnUtils.TOKEN_SEPERATOR);
        if (decryptedToken.length == 1) {
            this.logService.saveLog(new Log().action(Action.ERROR).message(
                    "Reset password error : The given token \"" + token + "\" is not valid ")
                    .createdAt(LocalDateTime.now()));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Reset password error",
                    "The given token is not valid");
        }
        String idUser = decryptedToken[0];
        LocalDateTime timeSentEmail = LocalDateTime.parse(decryptedToken[1]);
        if (LocalDateTime.now().isAfter(timeSentEmail
                .plus(Long.parseLong(configurationAppProperties.getExpirationLinkTime()), ChronoUnit.MINUTES))) {
            this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now())
                    .message("Reset password error : Link provided has expired"));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Reset password error",
                    "Link expired, go to the reset password page to generate a new link");
        }
        Optional<User> optionalUser = this.findUserById(idUser);
        if (optionalUser.isEmpty()) {
            this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now())
                    .message("Reset password error : no user who match id \"" + idUser + "\""));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Reset password error",
                    "No user found with infos given in token");
        } else {
            this.logService.saveLog(new Log().action(Action.UPDATE).createdAt(LocalDateTime.now())
                    .message("Updating password for user with id \"" + idUser + "\""));
            optionalUser.get().passwordHash(this.passwordEncoder.encode(password)).updatedAt(LocalDateTime.now());
        }
    }

    public void initResetPassword(String username, String email) {
        Optional<User> optionalUser = this.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now())
                    .message("Reset password error : Email \"" + email + "\" not found"));
            throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Reset password error", "Email not found");
        } else {
            if (!optionalUser.get().getLogin().equalsIgnoreCase(username)) {
                this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now())
                        .message("Reset password error : Username invalid for account \"" + optionalUser.get().getId()
                                + "\" with \"" + email + "\""));
                throw new HttpErrorException(HttpStatus.BAD_REQUEST, "Reset password error",
                        "Invalid username provided");
            }
            try {
                this.mailService.sendResetPasswordMail(optionalUser.get());
            } catch (MessagingException ex) {
                this.logService.saveLog(new Log().action(Action.ERROR).createdAt(LocalDateTime.now()).message(
                        "Reset password error : an error occured while sending email cause ==> " + ex.getMessage()));
                log.error(
                        "An error occured while sending the email to \"" + email + "\"", ex);
            }
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
