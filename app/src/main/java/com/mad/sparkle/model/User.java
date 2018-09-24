package com.mad.sparkle.model;

public class User {

    private int mUserId;
    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mMobilePhone;

    public User() {
    }

    public User(int userId, String email, String username, String password, String mobilePhone) {
        mUserId = userId;
        mEmail = email;
        mUsername = username;
        mPassword = password;
        mMobilePhone = mobilePhone;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        mMobilePhone = mobilePhone;
    }
}
