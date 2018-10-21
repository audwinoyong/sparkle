package com.mad.sparkle.model.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Nearby Search response mapped from JSON to Java objects
 */
public class NearbySearchResponse {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
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
     * Get next page token
     *
     * @return next page token
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * Set next page token
     *
     * @param nextPageToken the next page token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * Get results
     *
     * @return results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Set results
     *
     * @param results the results
     */
    public void setResults(List<Result> results) {
        this.results = results;
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
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}