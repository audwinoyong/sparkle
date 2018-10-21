package com.mad.sparkle.model.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Nearby Search geometry object.
 */
public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;

    /**
     * Get location
     *
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set location
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

}