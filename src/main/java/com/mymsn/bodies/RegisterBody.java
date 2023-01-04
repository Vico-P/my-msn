package com.mymsn.bodies;

import java.util.regex.Pattern;

import com.mymsn.utils.MyMsnUtils;

// Body received from front when user is creating an account
public class RegisterBody {
    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Check if the body sent by the front is valid
    public boolean isValid() {
        return !this.confirmPassword.isBlank() && !this.password.isBlank() && !this.username.isBlank()
                && !this.email.isBlank() && this.confirmPassword.equals(this.password)
                && Pattern.compile(MyMsnUtils.REGEX_EMAIL).matcher(this.email).matches();
    }
}
