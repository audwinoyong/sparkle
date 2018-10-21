package com.mad.sparkle.model;

public class Booking {

    public String storeId;
    public String storeName;
    public String date;
    public String time;

    public Booking() {
    }

    public Booking(String storeId, String storeName, String date, String time) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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