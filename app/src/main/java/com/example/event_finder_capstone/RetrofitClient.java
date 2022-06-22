package com.example.event_finder_capstone;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static EventEndpointsInterface Yelp_API;
    public static final String TAG = "Main Activity";
    public static final String YELP_API_BASE_URL = "https://api.yelp.com/v3/";

    private EventEndpointsInterface Yelp_Api;
    private static RetrofitClient instance = null;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Yelp_Api = retrofit.create(EventEndpointsInterface.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public EventEndpointsInterface getYelpAPI() {
        return Yelp_Api;
    }

}
