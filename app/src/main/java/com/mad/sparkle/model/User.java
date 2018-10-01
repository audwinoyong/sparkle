package com.mad.sparkle.model;

public class User {

    public String mEmail;
    public String mPassword;
    public String mFirstName;
    public String mLastName;
    public String mMobilePhone;

    public User() {
    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public User(String email, String password, String firstName, String lastName, String mobilePhone) {
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mMobilePhone = mobilePhone;
    }
}
