package com.mad.sparkle.model;

public class Store {
    private String name;
    private String address;
    private int distance;
    private int rating;
    private String phone;
//    private ArrayList<Service> services;

    public Store() {
    }

    public Store(String name, String address, int distance, int rating, String phone) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
//        mServices = services;
        this.phone = phone;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

//    public ArrayList<Service> getServices() {
//        return services;
//    }
//
//    public void setServices(ArrayList<Service> services) {
//        this.services = services;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
