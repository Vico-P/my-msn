package com.mymsn.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mail")
public class MailAppProperties {
    private String username;

    private String password;

    private String host;

    private String port;

    private Boolean enablettls;

    private Boolean auth;

    private String fromMail;

    public MailAppProperties() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Boolean isEnablettls() {
        return this.enablettls;
    }

    public Boolean getEnablettls() {
        return this.enablettls;
    }

    public void setEnablettls(Boolean enablettls) {
        this.enablettls = enablettls;
    }

    public Boolean isAuth() {
        return this.auth;
    }

    public Boolean getAuth() {
        return this.auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public String getFromMail() {
        return this.fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }
}
