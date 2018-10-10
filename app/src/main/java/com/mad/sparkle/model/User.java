package com.mad.sparkle.model;

public class User {

    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String mobilePhone;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String firstName, String lastName, String mobilePhone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhone = mobilePhone;
    }
}
