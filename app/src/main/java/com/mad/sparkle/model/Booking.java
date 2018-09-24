package com.mad.sparkle.model;

public class Booking {

    private int mBookingId;
    private String mDate;
    private String mTime;
    private Service mService;
    private User mUser;

    public Booking() {
    }

    public Booking(int bookingId, String date, String time, Service service, User user) {
        mBookingId = bookingId;
        mDate = date;
        mTime = time;
        mService = service;
        mUser = user;
    }

    public int getBookingId() {
        return mBookingId;
    }

    public void setBookingId(int bookingId) {
        mBookingId = bookingId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public Service getService() {
        return mService;
    }

    public void setService(Service service) {
        mService = service;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
