package com.mad.sparkle.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client class that generates an instance of Retrofit client.
 */
public class RetrofitClient {

    /**
     * Generates an instance of Retrofit client.
     *
     * @return Retrofit instance
     */
    public static Retrofit getClient() {
        // For logging Http requests
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(80, TimeUnit.SECONDS)
                .connectTimeout(80, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        // Return a new instance of Retrofit
        return new Retrofit.Builder()
                .baseUrl(Constants.PLACES_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
