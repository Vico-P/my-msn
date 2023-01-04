package com.mymsn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.mymsn.entities.User;
import com.mymsn.properties.ConfigurationAppProperties;
import com.mymsn.properties.MailAppProperties;
import com.mymsn.utils.MyMsnUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    private final MailAppProperties mailAppProperties;

    private final SpringTemplateEngine springTemplateEngine;

    private final ConfigurationAppProperties configurationAppProperties;

    @Autowired
    public MailService(JavaMailSender mailSender, MailAppProperties mailAppProperties,
            SpringTemplateEngine springTemplateEngine, ConfigurationAppProperties configurationAppProperties,
            LogService logService) {
        this.mailSender = mailSender;
        this.mailAppProperties = mailAppProperties;
        this.springTemplateEngine = springTemplateEngine;
        this.configurationAppProperties = configurationAppProperties;
    }

    @Async
    public void sendVerifyEmail(User user)
            throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("baseUrl", configurationAppProperties.getBaseUrl());
        thymeleafContext.setVariable("url", configurationAppProperties.getBaseUrl() + "/verify-email?token="
                + MyMsnUtils.encrypt(user.getEmail() + ":" + user.getId()));
        thymeleafContext.setVariable("login",
                user.getLogin().substring(0, 1).toUpperCase() + user.getLogin().substring(1).toLowerCase());
        String htmlBody = this.springTemplateEngine.process("email-validation.html", thymeleafContext);

        sendSimpleMessage(user.getEmail(), "Verify your email", htmlBody, true);
    }

    @Async
    public void sendSimpleMessage(
            String to, String subject, String text, boolean isHtml) throws MailException, MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailAppProperties.getFromMail());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);
        this.mailSender.send(message);
    }
}
