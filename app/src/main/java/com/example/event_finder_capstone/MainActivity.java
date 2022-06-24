package com.example.event_finder_capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_finder_capstone.models.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseUser;

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
        String eventSearchRegion = "en_US";
        Long eventSearchRadiusFromUser = 40000L;
        String numberOfEventsToRetrieve = "10";
        Long upcomingEventsOnly =  (System.currentTimeMillis() / 1000L);
        RetrofitClient.getInstance().getYelpAPI().getEvents(eventSearchRegion,
                numberOfEventsToRetrieve,
                upcomingEventsOnly,
                eventSearchRadiusFromUser,
                ParseUser.getCurrentUser().getString("zip")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    runOnUiThread(() -> {
                        try {
                            JsonObject result = response.body();
                            eventsList.clear();
                            eventsList.addAll(convertToList(result));
                            eventsAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "JSON exception error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to call API", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnLogout) {
            onLogoutButton();
        }
        return true;
    }

    void onLogoutButton() {
        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}