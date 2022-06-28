package com.example.event_finder_capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_finder_capstone.models.Event;
import com.example.event_finder_capstone.models.Photo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumActivity extends AppCompatActivity {

    private static final String TAG = "PhotoAlbumActivity";
    private static final int POST_LIMIT = 20;
    Event event;
    RecyclerView rvPhotos;
    protected PhotoAdapter photo_adapter;
    protected List<Photo> allPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        rvPhotos = findViewById(R.id.rvPhotos);
        allPhotos = new ArrayList<>();
        photo_adapter = new PhotoAdapter(this, allPhotos);
        rvPhotos.setAdapter(photo_adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvPhotos.setLayoutManager(gridLayoutManager);
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        queryPhotos(event.getId());
    }

    private void queryPhotos(String eventId) {
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        query.whereEqualTo(Photo.KEY_EVENT_ID, eventId);
        query.include(Photo.KEY_USER);
        query.include(Photo.KEY_EVENT_ID);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e != null) {
                    Toast.makeText(PhotoAlbumActivity.this, "Failed to query posts", Toast.LENGTH_LONG).show();
                    return;
                }
                allPhotos.addAll(photos);
                photo_adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAddPhoto) {
            onAddImageButton();
        }
        return true;
    }

    void onAddImageButton() {
        Intent intent = new Intent(PhotoAlbumActivity.this, AddPhotoActivity.class);
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
