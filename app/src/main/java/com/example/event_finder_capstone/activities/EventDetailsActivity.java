package com.example.event_finder_capstone.activities;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.event_finder_capstone.R;
import com.example.event_finder_capstone.models.Event;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EventDetailsActivity extends AppCompatActivity {
    Event event;
    TextView tvEventDetailsTitle;
    TextView tvEventDetailsDescription;
    TextView tvEventDetailsStartDate;
    TextView tvEventDetailsEndDate;
    TextView tvEventDetailsAddress;
    TextView tvEventDetailsSite;
    TextView tvEventDetailsCost;
    ImageView ivEventDetailsImage;
    Button btnPhotoAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Event Details");

        tvEventDetailsTitle = findViewById(R.id.tvEventDetailsTitle);
        tvEventDetailsDescription = findViewById(R.id.tvEventDetailsDescription);
        tvEventDetailsStartDate = findViewById(R.id.tvEventDetailsStartDate);
        tvEventDetailsEndDate = findViewById(R.id.tvEventDetailsEndDate);
        tvEventDetailsAddress = findViewById(R.id.tvEventDetailsAddress);
        tvEventDetailsSite = findViewById(R.id.tvEventDetailsSite);
        tvEventDetailsCost = findViewById(R.id.tvEventDetailsCost);
        ivEventDetailsImage = findViewById(R.id.ivEventsDetailsImage);
        btnPhotoAlbum = findViewById(R.id.btnPhotoAlbum);

        setDetailsScreenText();
        setUpDoubleTap();

        btnPhotoAlbum.setOnClickListener(v -> goPhotoAlbum());
    }

    private void setUpDoubleTap() {
        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(getApplicationContext(), "Bookmarked", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        final GestureDetector detector = new GestureDetector(listener);
        detector.setOnDoubleTapListener(listener);
        getWindow().getDecorView().setOnTouchListener((view, event) -> detector.onTouchEvent(event));
    }

    private void goPhotoAlbum() {
        Intent intent = new Intent(EventDetailsActivity.this, PhotoAlbumActivity.class);
        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
        startActivity(intent);
    }

    private void setDetailsScreenText() {
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        tvEventDetailsTitle.setText(event.getName());
        tvEventDetailsDescription.setText(event.getDescription());
        formatAndSetEventURL();
        tvEventDetailsAddress.setText(event.getLocation());
        tvEventDetailsCost.setText(event.getCost());
        Glide.with(this).load(event.getImageUrl()).transform(new CenterCrop(), new RoundedCorners(30)).into(ivEventDetailsImage);
        setEventStartAndEndDates();
    }

    private void setEventStartAndEndDates() {
        try {
            tvEventDetailsStartDate.setText(convertEventDateFormat(event.getTimeStart()));
        } catch (ParseException e) {
            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve start date", Toast.LENGTH_SHORT).show();
        }
        try {
            tvEventDetailsEndDate.setText(convertEventDateFormat(event.getTimeEnd()));
        } catch (ParseException e) {
            Toast.makeText(EventDetailsActivity.this, "Failed to retrieve end date", Toast.LENGTH_SHORT).show();
        }
    }

    private void formatAndSetEventURL() {
        String link = event.getEventSiteUrl();
        tvEventDetailsSite.setMovementMethod(LinkMovementMethod.getInstance());
        tvEventDetailsSite.setClickable(true);
        String text = "<a href='" + link + "'> Book Tickets and Learn More </a>";
        tvEventDetailsSite.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private String convertEventDateFormat(String unformattedDate) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date formattedDate = inputFormat.parse(unformattedDate);
        return outputFormat.format(formattedDate);
    }


}

