package com.mad.sparkle.model;

public class Booking {

    private String date;
    private String time;
    private User user;

    public Booking() {
    }

    public Booking(String date, String time, User user) {
        this.date = date;
        this.time = time;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
