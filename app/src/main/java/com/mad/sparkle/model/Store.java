package com.mad.sparkle.model;

import java.util.ArrayList;

public class Store {
    private String mName;
    private String mAddress;
    private int mRating;
    private int mDistance;
    private ArrayList<Service> mServices;

    public Store() {
    }

    public Store(String name, String address, int rating, int distance, ArrayList<Service> services) {
        mName = name;
        mAddress = address;
        mRating = rating;
        mDistance = distance;
        mServices = services;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public ArrayList<Service> getServices() {
        return mServices;
    }

    public void setServices(ArrayList<Service> services) {
        mServices = services;
    }
}
