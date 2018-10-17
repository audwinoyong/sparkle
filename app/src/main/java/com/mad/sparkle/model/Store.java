package com.mad.sparkle.model;

public class Store {

    private String name;
    private String address;
    private int distance;
    private double rating;
    private String phone;
    private double latitude;
    private double longitude;
    private String photoReference;

    public Store() {
    }

    public Store(String name, String address, int distance, double rating, String phone, double latitude, double longitude, String photoReference) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoReference = photoReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
