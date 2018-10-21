package com.mad.sparkle.model;

/**
 * Store model class
 */
public class Store {

    public String storeId;
    public String name;
    public String address;
    public int distance;
    public double rating;
    public String phone;
    public double latitude;
    public double longitude;
    public String photoReference;

    /**
     * Empty constructor
     */
    public Store() {
    }

    /**
     * Store constructor
     *
     * @param storeId        store id
     * @param name           store name
     * @param address        address
     * @param distance       distance
     * @param rating         rating
     * @param phone          phone
     * @param latitude       latitude
     * @param longitude      longitude
     * @param photoReference photo reference
     */
    public Store(String storeId, String name, String address, int distance, double rating, String phone, double latitude, double longitude, String photoReference) {
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.rating = rating;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoReference = photoReference;
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
     * Get name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get address
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     *
     * @param address address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get distance
     *
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Set distance
     *
     * @param distance distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Get rating
     *
     * @return rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set rating
     *
     * @param rating rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get phone
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set phone
     *
     * @param phone phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get latitude
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set latitude
     *
     * @param latitude latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get longitude
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set longitude
     *
     * @param longitude longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get photo reference
     *
     * @return photo reference
     */
    public String getPhotoReference() {
        return photoReference;
    }

    /**
     * Set photo reference
     *
     * @param photoReference photo reference
     */
    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
