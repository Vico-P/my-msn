package com.mymsn.dtos;

import java.io.Serializable;
import java.util.UUID;

import com.mymsn.entities.User;

public class UserDto implements Serializable {
    private UUID id;

    private String email;

    private String username;

    public UserDto() {
    }

    public UserDto(User user) {
        this.username = user.getLogin();
        this.email = user.getEmail();
        this.id = user.getId();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
