package com.example.event_finder_capstone;

import android.app.Application;

import com.example.event_finder_capstone.models.Photo;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    String parse_app_id = BuildConfig.PARSE_APP_ID;
    String parse_client_key = BuildConfig.PARSE_CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Photo.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(parse_app_id)
                .clientKey(parse_client_key)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
