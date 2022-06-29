package com.example.event_finder_capstone;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.event_finder_capstone.models.Event;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {
    Context context;
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

        btnPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoAlbum();
            }
        });
    }

    private void goPhotoAlbum() {
        Intent intent = new Intent(EventDetailsActivity.this, PhotoAlbumActivity.class);
        startActivity(intent);
    }

    private void setDetailsScreenText() {
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        tvEventDetailsTitle.setText(event.getName());
        tvEventDetailsDescription.setText(event.getDescription());
        formatAndSetEventURL();
        tvEventDetailsAddress.setText(event.getLocation());
        tvEventDetailsCost.setText(event.getCost());
        Glide.with(this).load(event.getImageUrl()).centerCrop().into(ivEventDetailsImage);
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
        tvEventDetailsSite.setText(Html.fromHtml(text));
    }

    private String convertEventDateFormat(String unformattedDate) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date formattedDate = inputFormat.parse(unformattedDate);
        return outputFormat.format(formattedDate);
    }


}

