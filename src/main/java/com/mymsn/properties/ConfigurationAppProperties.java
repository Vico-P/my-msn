package com.mymsn.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("configuration")
public class ConfigurationAppProperties {
    private String secret;

    private String baseUrl;

    private String expirationLinkTime;

    public ConfigurationAppProperties() {
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getExpirationLinkTime() {
        return this.expirationLinkTime;
    }

    public void setExpirationLinkTime(String expirationLinkTime) {
        this.expirationLinkTime = expirationLinkTime;
    }

}
