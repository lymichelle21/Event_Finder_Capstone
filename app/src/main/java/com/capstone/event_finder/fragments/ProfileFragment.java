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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.MainActivity;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Bookmark;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.EventViewModel;
import com.capstone.event_finder.network.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private final List<Bookmark> userBookmarks = new ArrayList<>();
    private final List<Event> bookmarkList = new ArrayList<>();
    private final ArrayList<String> bookmarkIds = new ArrayList<>();
    RecyclerView rvBookmarks;
    TextView tvProfileUsername;
    TextView tvProfileBio;
    TextView tvInterestCategories;
    EventsAdapter bookmarkAdapter;
    private EventViewModel eventViewModel;

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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileBio = view.findViewById(R.id.tvProfileBio);
        tvInterestCategories = view.findViewById(R.id.tvInterestCategories);
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvProfileBio.setText(ParseUser.getCurrentUser().getString("bio"));
        tvInterestCategories.setText(ParseUser.getCurrentUser().getString("event_categories_string"));

        JsonArray allBookmarks = new JsonArray();
        setUpRecyclerView(view);
        getAndSetUserBookmarks(allBookmarks);
    }

    public void getAndSetUserBookmarks(JsonArray allBookmarks) {
        bookmarkList.clear();
        userBookmarks.clear();
        bookmarkIds.clear();
        queryUserBookmarksFromParse(allBookmarks);
    }

    private void tryRetrieveEventInCache(String eventId, JsonArray allBookmarks) {
        eventViewModel.eventInCache(eventId).observe(getViewLifecycleOwner(), events -> {
            if (events.isEmpty()) {
                lookupEventsAndSetEvents(eventId, allBookmarks);
            }
            bookmarkList.addAll(events);
            bookmarkAdapter.notifyDataSetChanged();
        });
    }

    public void queryUserBookmarksFromParse(JsonArray allBookmarks) {
        final int POST_LIMIT = 10;
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.whereEqualTo(Bookmark.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground((bookmarks, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Failed to find bookmarks", Toast.LENGTH_LONG).show();
                return;
            }
            userBookmarks.clear();
            userBookmarks.addAll(bookmarks);
            queryUserBookmarkIds(allBookmarks);
        });
    }

    private void queryUserBookmarkIds(JsonArray allBookmarks) {
        for (int i = 0; i < userBookmarks.size(); i++) {
            Bookmark bookmark = userBookmarks.get(i);
            String bookmarkId = bookmark.getEventId();
            bookmarkIds.add(bookmarkId);
        }
        queryAndSetUserBookmarksToFeed(allBookmarks);
    }

    private void queryAndSetUserBookmarksToFeed(JsonArray allBookmarks) {
        for (int i = 0; i < bookmarkIds.size(); i++) {
            //eventViewModel.tryRetrieveEventInCache(bookmarkList, bookmarkIds.get(i), allBookmarks, ProfileFragment.this);
            tryRetrieveEventInCache(bookmarkIds.get(i), allBookmarks);
        }
        bookmarkAdapter.notifyDataSetChanged();
    }

    public void lookupEventsAndSetEvents(String bookmarkId, JsonArray allBookmarks) {
        RetrofitClient.getInstance().getYelpAPI().lookupEvent(bookmarkId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject result = response.body();
                    allBookmarks.add(result);
                } else {
                    Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
                }
                bookmarkList.addAll(convertToList(allBookmarks));
                bookmarkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to get events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        bookmarkAdapter = new EventsAdapter(getContext(), bookmarkList);
        rvBookmarks.setAdapter(bookmarkAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvBookmarks.setLayoutManager(linearLayoutManager);
    }

    private Collection<? extends Event> convertToList(JsonArray result) {
        List<Event> res = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JsonObject temp = (JsonObject) result.get(i);
            Event event = new Event();
            ((MainActivity) requireActivity()).populateEventInfo(event, temp);
            res.add(event);
        }
        return res;
    }
}