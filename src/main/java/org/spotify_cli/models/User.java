package org.spotify_cli.models;

import java.util.Date;

public class User {
    private String username;
    private String password;
    private String email;
    private String last_login;
    private int login_attempts;
    private boolean account_locked;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", last_login='" + last_login + '\'' +
                ", login_attempts=" + login_attempts +
                ", account_locked=" + account_locked +
                '}';
    }

    public User(String username, String password, String email, String last_login, int login_attempts, boolean account_locked) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.last_login = last_login;
        this.login_attempts = login_attempts;
        this.account_locked = account_locked;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public int getLogin_attempts() {
        return login_attempts;
    }

    public void setLogin_attempts(int login_attempts) {
        this.login_attempts = login_attempts;
    }

    public boolean isAccount_locked() {
        return account_locked;
    }

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }

    public User() {}



}
