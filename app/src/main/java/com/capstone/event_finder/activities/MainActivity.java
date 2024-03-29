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
import com.capstone.event_finder.network.EventViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setUpBottomNavigation(fragmentManager);
    }

    private void setUpBottomNavigation(FragmentManager fragmentManager) {
        FeedFragment feedFragment = new FeedFragment();
        ExploreFragment exploreFragment = new ExploreFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        eventViewModel.refreshEvents();
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
        eventViewModel.clearCache();
        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}