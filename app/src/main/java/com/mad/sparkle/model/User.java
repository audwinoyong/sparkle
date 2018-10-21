package com.mad.sparkle.model;

/**
 * User model class
 */
public class User {

    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String mobilePhone;
    public String profileImage;

    /**
     * Empty constructor
     */
    public User() {
    }

    /**
     * User constructor
     *
     * @param email        email
     * @param password     password
     * @param firstName    first name
     * @param lastName     last name
     * @param mobilePhone  mobile phone
     * @param profileImage profile image
     */
    public User(String email, String password, String firstName, String lastName, String mobilePhone, String profileImage) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhone = mobilePhone;
        this.profileImage = profileImage;
    }

    /**
     * Get email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get first name
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set first name
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get last name
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set last name
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get mobile phone
     *
     * @return mobile phone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Set mobile phone
     *
     * @param mobilePhone mobile phone
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Get profile image
     *
     * @return profile image
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * Set profile image
     *
     * @param profileImage profile image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
