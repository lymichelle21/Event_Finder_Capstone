package com.capstone.event_finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.event_finder.R;
import com.capstone.event_finder.adapters.PhotoAdapter;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhotoAlbumActivity extends AppCompatActivity {

    private static final int POST_LIMIT = 20;
    protected PhotoAdapter photoAdapter;
    protected List<Photo> allPhotos;
    Event event;
    RecyclerView rvPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Photos from " + event.getName());

        setUpPhotoGridRecyclerView();
        queryPhotos(event.getId());
    }

    private void setUpPhotoGridRecyclerView() {
        rvPhotos = findViewById(R.id.rvPhotos);
        allPhotos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, allPhotos);
        rvPhotos.setAdapter(photoAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvPhotos.setLayoutManager(gridLayoutManager);
    }

    private void queryPhotos(String eventId) {
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        query.whereEqualTo(Photo.KEY_EVENT_ID, eventId);
        query.include(Photo.KEY_USER);
        query.include(Photo.KEY_EVENT_ID);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground((photos, e) -> {
            if (e != null) {
                Toast.makeText(PhotoAlbumActivity.this, "Failed to query posts", Toast.LENGTH_LONG).show();
                return;
            }
            allPhotos.addAll(photos);
            photoAdapter.notifyDataSetChanged();
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
