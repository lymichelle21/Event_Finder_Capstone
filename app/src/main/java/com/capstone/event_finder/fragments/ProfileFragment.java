package com.capstone.event_finder.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Bookmark;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    RecyclerView rvBookmarks;
    TextView tvProfileUsername;
    EventsAdapter bookmarkAdapter;
    JSONArray allBookmarks;
    private List<Bookmark> userBookmarks = new ArrayList<Bookmark>();
    private List<Event> bookmarkList = new ArrayList<>();
    private ArrayList bookmarkIds = new ArrayList();

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setUpRecyclerView(view);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        queryUserBookmarksFromParse();
        //lookupEvent();
    }

    public void queryUserBookmarksFromParse(){
        final int POST_LIMIT = 10;
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Bookmark>() {
            @Override
            public void done(List<Bookmark> bookmarks, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Failed to find bookmarks", Toast.LENGTH_LONG).show();
                    return;
                }
                userBookmarks.addAll(bookmarks);
                for (int i = 0; i < userBookmarks.size(); i++) {
                    Bookmark bookmark = bookmarks.get(i);
                    String bookmarkId = bookmark.getEventId();
                    bookmarkIds.add(bookmarkId);
                    Log.d(TAG, bookmarkIds.toString());
                }
            }
        });
    }

    public void lookupEvent() {
        RetrofitClient.getInstance().getYelpAPI().lookupEvent("oakland-saucy-oakland-restaurant-pop-up").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    //Log.d(TAG, "This is the query: " + result.toString());
                    Log.d(TAG, allBookmarks.toString());
                    Toast.makeText(getContext(), "Query Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        bookmarkList = new ArrayList<>();
        bookmarkAdapter = new EventsAdapter(getContext(), bookmarkList);
        rvBookmarks.setAdapter(bookmarkAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvBookmarks.setLayoutManager(linearLayoutManager);
    }

    private Collection<? extends Event> convertToList(JsonObject result) {
        List<Event> res = new ArrayList<>();
        JsonArray bookmarks = result.getAsJsonArray();
        for (int i = 0; i < bookmarks.size(); i++) {
            JsonObject temp = (JsonObject) bookmarks.get(i);
            Event event = new Event();
            populateEventInfo(event, temp);
            res.add(event);
        }
        return res;
    }

    private void populateEventInfo(Event event, JsonObject temp) {
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

}