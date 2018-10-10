package com.mad.sparkle.model;

public class Booking {

    private int bookingId;
    private String date;
    private String time;
    private Service service;
    private User user;

    public Booking() {
    }

    public Booking(int bookingId, String date, String time, Service service, User user) {
        this.bookingId = bookingId;
        this.date = date;
        this.time = time;
        this.service = service;
        this.user = user;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
