package com.capstone.event_finder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.EventsAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.network.EventViewModel;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    RecyclerView rvBookmarks;
    TextView tvProfileUsername;
    TextView tvProfileBio;
    TextView tvInterestCategories;
    ImageView ivProfileImage;
    EventsAdapter bookmarkAdapter;
    private final List<Event> bookmarkList = new ArrayList<>();
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
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvInterestCategories = view.findViewById(R.id.tvInterestCategories);
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvProfileBio.setText(ParseUser.getCurrentUser().getString("bio"));
        tvInterestCategories.setText(ParseUser.getCurrentUser().getString("event_categories_string"));
        Glide.with(view.getContext()).load(Objects.requireNonNull(ParseUser.getCurrentUser().getParseFile("profile_image")).getUrl()).centerCrop().transform(new CenterCrop(), new CircleCrop()).into(ivProfileImage);

        setUpRecyclerView(view);
        getUserBookmarks();
    }

    private void getUserBookmarks() {
        eventViewModel.getBookmarks(ParseUser.getCurrentUser()).observe(getViewLifecycleOwner(), events -> {
            bookmarkList.clear();
            bookmarkList.addAll(events);
            bookmarkAdapter.notifyDataSetChanged();
        });
    }

    private void setUpRecyclerView(@NonNull View view) {
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        bookmarkAdapter = new EventsAdapter(getContext(), bookmarkList);
        rvBookmarks.setAdapter(bookmarkAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvBookmarks.setLayoutManager(linearLayoutManager);
    }
}