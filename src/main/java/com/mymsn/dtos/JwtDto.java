package com.mymsn.dtos;

import java.io.Serializable;

// Object to return when Token is generated
public class JwtDto implements Serializable {

    private String token;

    public JwtDto() {
    }

    public JwtDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
