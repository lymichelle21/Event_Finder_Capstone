package com.capstone.event_finder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.capstone.event_finder.R;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    TextView tvProfileUsername;

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

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
    }

}