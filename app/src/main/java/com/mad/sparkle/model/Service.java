package com.mad.sparkle.model;

public class Service {

    private int mServiceId;
    private String mServiceName;
    private int mPrice;

    public Service() {
    }

    public Service(int serviceId, String serviceName, int price) {
        mServiceId = serviceId;
        mServiceName = serviceName;
        mPrice = price;
    }

    public int getServiceId() {
        return mServiceId;
    }

    public void setServiceId(int serviceId) {
        mServiceId = serviceId;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        mServiceName = serviceName;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }
}
