package com.example.event_finder_capstone.network;

import com.example.event_finder_capstone.BuildConfig;
import com.example.event_finder_capstone.interfaces.EventEndpointsInterface;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String YELP_API_BASE_URL = "https://api.yelp.com/v3/";
    public static final String YELP_KEY_WITH_FORMATTING = "Bearer " + BuildConfig.YELP_KEY;
    private static RetrofitClient instance = null;
    private final EventEndpointsInterface yelpApi;

    private RetrofitClient() {

        Interceptor interceptor = chain -> {
            Request newRequest = chain.request().newBuilder().addHeader("Authorization", YELP_KEY_WITH_FORMATTING).build();
            return chain.proceed(newRequest);
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        yelpApi = retrofit.create(EventEndpointsInterface.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public EventEndpointsInterface getYelpAPI() {
        return yelpApi;
    }

}
