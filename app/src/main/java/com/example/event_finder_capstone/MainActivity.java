package com.example.event_finder_capstone;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_finder_capstone.models.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvEvents;
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvEvents = findViewById(R.id.rvEvents);
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(this, eventsList);
        rvEvents.setAdapter(eventsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvEvents.setLayoutManager(linearLayoutManager);

        getAPIEvents();
    }

    private void getAPIEvents() {
        RetrofitClient.getInstance().getYelpAPI().getEvents("en_US", "10", "94025").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    runOnUiThread(() -> {
                        try {
                            JsonObject result = response.body();
                            eventsList.clear();
                            eventsList.addAll(convertToList(result));
                            eventsAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("error", "JSON exception error");
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private Collection<? extends Event> convertToList(JsonObject result) {
        List<Event> res = new ArrayList<>();
        JsonArray events = result.getAsJsonArray("events");
        for (int i = 0; i < events.size(); i++) {
            JsonObject temp = (JsonObject) events.get(i);
            Event event = new Event();
            event.setName(temp.get("name").getAsString());
            event.setDescription(temp.get("description").getAsString());
            event.setImageUrl(temp.get("image_url").getAsString());
            event.setTimeStart(temp.get("time_start").getAsString());
            res.add(event);
        }
        return res;
    }
}