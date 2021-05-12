package com.example.keepmoneyv3.utility;

import java.io.Serializable;

public class User implements Serializable {
    private String name, surname;
    private String username, password, email;
    private float total;

    public User(String username, String password, String name, String surname, String email, float total) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.total = total;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public float getTotal() {
        return total;
    }
}
