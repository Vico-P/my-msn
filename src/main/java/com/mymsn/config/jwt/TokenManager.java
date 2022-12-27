package com.mymsn.config.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mymsn.properties.ConfigurationAppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenManager {

    private final ConfigurationAppProperties applicationProperties;

    private final Key hmacKey;

    @Autowired
    public TokenManager(ConfigurationAppProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.hmacKey = new SecretKeySpec(Base64.getDecoder().decode(this.applicationProperties.getSecret()),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateJwtToken(String login) {
        return Jwts.builder().claim("login", login).setSubject(login).setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES))).signWith(this.hmacKey).compact();
    }

    public Jws<Claims> parseJwt(String jwtString) throws SignatureException {
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(this.hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }
}
