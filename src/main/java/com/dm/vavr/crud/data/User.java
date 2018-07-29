package com.dm.vavr.crud.data;

import org.springframework.data.annotation.Id;

public class User implements Identifiable<String> {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    public User() {
    }

    public User(final String id, final String username, final String email, final String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
