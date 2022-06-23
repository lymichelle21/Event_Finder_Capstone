package com.example.event_finder_capstone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.event_finder_capstone.models.Event;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAPIEvents();
    }

    private void getAPIEvents() {
        RetrofitClient.getInstance().getYelpAPI().getEvents("en_US").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                return;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}