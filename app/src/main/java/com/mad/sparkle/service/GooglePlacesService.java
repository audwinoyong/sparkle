package com.mad.sparkle.service;

import com.mad.sparkle.model.nearbysearch.NearbySearchResponse;
import com.mad.sparkle.model.placedetails.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service class for Google Places API HTTP requests
 */
public interface GooglePlacesService {

    /**
     * Get the nearby car wash.
     *
     * @param location Current location
     * @param key      Google Maps API key
     * @return Nearby Search Response
     */
    @GET("api/place/nearbysearch/json?rankby=distance&keyword=car+wash")
    Call<NearbySearchResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("key") String key
    );

    /**
     * Get the specified place formatted phone number.
     *
     * @param placeid The place id
     * @param key     Google Maps API key
     * @return Place Details Response
     */
    @GET("api/place/details/json?fields=formatted_phone_number")
    Call<PlaceDetailsResponse> getPlaceDetails(
            @Query("placeid") String placeid,
            @Query("key") String key
    );
}