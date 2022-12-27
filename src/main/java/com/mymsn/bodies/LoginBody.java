package com.mymsn.bodies;

import java.io.Serializable;

// Body received from front when user is authenticating
public class LoginBody implements Serializable {
    private String login;
    private String password;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
