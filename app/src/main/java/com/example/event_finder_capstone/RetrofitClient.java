package com.example.event_finder_capstone;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String TAG = "Main Activity";
    public static final String YELP_API_BASE_URL = "https://api.yelp.com/v3/";
    public static final String value = "Bearer " + BuildConfig.YELP_KEY;
    public static EventEndpointsInterface Yelp_API;
    private static RetrofitClient instance = null;
    private final EventEndpointsInterface Yelp_Api;

    private RetrofitClient() {

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", value).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YELP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
