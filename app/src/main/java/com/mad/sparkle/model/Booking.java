package com.mad.sparkle.model;

/**
 * Booking model class
 */
public class Booking {

    public String storeId;
    public String storeName;
    public String date;
    public String time;

    /**
     * Empty constructor
     */
    public Booking() {
    }

    /**
     * Booking constructor
     *
     * @param storeId   store id
     * @param storeName store name
     * @param date      booking date
     * @param time      booking time
     */
    public Booking(String storeId, String storeName, String date, String time) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.date = date;
        this.time = time;
    }

    /**
     * Get store id
     *
     * @return store id
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Set store id
     *
     * @param storeId store id
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * Get store name
     *
     * @return store name
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Set store name
     *
     * @param storeName the store name
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Get booking date
     *
     * @return booking date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set booking date
     *
     * @param date booking date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get booking time
     *
     * @return booking time
     */
    public String getTime() {
        return time;
    }

    /**
     * Set booking time
     *
     * @param time booking time
     */
    public void setTime(String time) {
        this.time = time;
    }
}