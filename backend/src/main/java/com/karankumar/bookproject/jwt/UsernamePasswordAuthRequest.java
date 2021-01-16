package com.karankumar.bookproject.jwt;

public class UsernamePasswordAuthRequest {
    private String username;
    private String password;

    public UsernamePasswordAuthRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
