package com.mad.sparkle.model.placedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Place Details response mapped from JSON to Java objects
 */
public class PlaceDetailsResponse {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * Get Html attributions
     *
     * @return Html attributions
     */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * Set Html attributions
     *
     * @param htmlAttributions Html attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * Get result
     *
     * @return result
     */
    public Result getResult() {
        return result;
    }

    /**
     * Set result
     *
     * @param result result
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Get status
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status
     *
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}