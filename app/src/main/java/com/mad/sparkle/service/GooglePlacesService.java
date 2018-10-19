package com.mad.sparkle.service;

import com.mad.sparkle.model.nearbysearch.NearbySearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Service class for Google Places API HTTP requests
 */
public interface GooglePlacesService {

    @GET("api/place/nearbysearch/json?rankby=distance&keyword=car+wash")
    Call<NearbySearchResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("key") String key
    );
}