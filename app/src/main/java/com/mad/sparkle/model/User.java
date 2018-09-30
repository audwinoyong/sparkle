package com.mad.sparkle.model;

public class User {

    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mMobilePhone;

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, String mobilePhone) {
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mMobilePhone = mobilePhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        mMobilePhone = mobilePhone;
    }
}
