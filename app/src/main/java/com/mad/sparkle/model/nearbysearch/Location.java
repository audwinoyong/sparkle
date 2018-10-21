package com.mad.sparkle.model.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Nearby Search location object.
 */
public class Location {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    /**
     * Get the latitude
     *
     * @return latitude
     */
    public Double getLat() {
        return lat;
    }

    /**
     * Set the latitude
     *
     * @param lat latitude
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Get the longitude
     *
     * @return longitude
     */
    public Double getLng() {
        return lng;
    }

    /**
     * Set the longitude
     *
     * @param lng longitude
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

}
