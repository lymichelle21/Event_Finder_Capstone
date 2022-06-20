package com.example.event_finder_capstone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    String app_id = BuildConfig.APP_ID;
    String client_key = BuildConfig.CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(app_id)
                .clientKey(client_key)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
