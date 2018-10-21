package com.mad.sparkle.model;

public class Booking {

    public String storeName;
    public String date;
    public String time;

    public Booking() {
    }

    public Booking(String storeName, String date, String time) {
        this.storeName = storeName;
        this.date = date;
        this.time = time;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
}