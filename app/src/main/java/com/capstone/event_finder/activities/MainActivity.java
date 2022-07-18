package com.capstone.event_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.event_finder.R;
import com.capstone.event_finder.fragments.ExploreFragment;
import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.fragments.ProfileFragment;
import com.capstone.event_finder.interfaces.EventFetcherInterface;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.EventViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EventViewModel eventViewModel;
    private EventFetcherInterface feedFragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setUpBottomNavigation(fragmentManager);
    }

    public void setFeedFragmentListener(EventFetcherInterface feedFragmentListener) {
        this.feedFragmentListener = feedFragmentListener;
    }

    private void setUpBottomNavigation(FragmentManager fragmentManager) {
        Fragment feedFragment = new FeedFragment();
        Fragment exploreFragment = new ExploreFragment();
        Fragment profileFragment = new ProfileFragment();
        //initialCallToEventApi((EventFetcherInterface) feedFragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.action_feed:
                    fragment = feedFragment;
                    break;
                case R.id.action_explore:
                    fragment = exploreFragment;
                    break;
                case R.id.action_profile:
                    fragment = profileFragment;
                    break;
                default:
                    return true;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }

    public void populateEventInfo(Event event, JsonObject temp) {
        event.setName(temp.get("name").getAsString());
        event.setDescription(temp.get("description").getAsString());
        event.setImageUrl(temp.get("image_url").getAsString());
        event.setTimeStart(temp.get("time_start").getAsString());
        event.setId(temp.get("id").getAsString());
        event.setEventSiteUrl(temp.get("event_site_url").getAsString());
        event.setCategory(temp.get("category").getAsString());
        checkAndSetEventEndTime(event, temp);
        checkAndSetEventCost(event, temp);
        formatAndSetEventLocation(temp, event);
    }

    private void checkAndSetEventCost(Event event, JsonObject temp) {
        if (temp.get("cost").toString().matches("null")) {
            event.setCost("N/A");
        } else {
            event.setCost("$" + temp.get("cost").getAsString() + "0");
        }
    }

    private void checkAndSetEventEndTime(Event event, JsonObject temp) {
        if (temp.get("time_end").toString().matches("null")) {
            event.setTimeEnd(temp.get("time_start").getAsString());
        } else {
            event.setTimeEnd(temp.get("time_end").getAsString());
        }
    }

    private void formatAndSetEventLocation(JsonObject temp, Event event) {
        JsonArray formattedLocation = temp.getAsJsonObject("location").getAsJsonArray("display_address");
        StringBuilder formattedLocationString = new StringBuilder();
        for (int i = 0; i < formattedLocation.size(); i++) {
            formattedLocationString.append(formattedLocation.get(i).getAsString()).append(" ");
        }
        event.setLocation(formattedLocationString.toString());
    }

    private void initialCallToEventApi(EventFetcherInterface feedFragment) {
        setFeedFragmentListener(feedFragment);
        feedFragmentListener.getAPIEvents();
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
        eventViewModel.delete();
        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}