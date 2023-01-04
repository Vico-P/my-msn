package com.mymsn.config.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.mymsn.properties.MailAppProperties;

@Configuration
public class JavaMailSenderConfig {

    private final MailAppProperties mailAppProperties;

    @Autowired
    public JavaMailSenderConfig(MailAppProperties mailAppProperties) {
        this.mailAppProperties = mailAppProperties;
    }

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mail-templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    // Bean to create our JavaMailSender object, this will be use by Spring when we
    // inject a JavaMailSender dependency with @Autowired
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.mailAppProperties.getHost());
        mailSender.setPort(Integer.parseInt(this.mailAppProperties.getPort()));

        mailSender.setUsername(this.mailAppProperties.getUsername());
        mailSender.setPassword(this.mailAppProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", this.mailAppProperties.getAuth());
        props.put("mail.smtp.starttls.enable", this.mailAppProperties.getEnablettls());
        props.put("mail.debug", "true");

        return mailSender;
    }
}
