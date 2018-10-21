package com.mad.sparkle.model.placedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Result object for Place Details API request
 */
public class Result {

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;

    /**
     * Get formatted phone number
     *
     * @return formatted phone number
     */
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    /**
     * Set formatted phone number
     *
     * @param formattedPhoneNumber the formatted phone number
     */
    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }
}
