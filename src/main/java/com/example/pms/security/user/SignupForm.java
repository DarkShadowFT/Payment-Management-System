package com.example.pms.security.user;

public class SignupForm {
    private String username;
    private String password;


    public SignupForm() {
    }

    public SignupForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
