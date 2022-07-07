package com.capstone.event_finder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.capstone.event_finder.R;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.capstone.event_finder.utils.BitmapScaler;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AddPhotoActivity extends AppCompatActivity {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1989;
    static int PHOTO_WIDTH = 300;
    static int PHOTO_QUALITY = 40;
    public String photoFileName = "photo.jpg";
    Event event;
    String TAG = "AddPhotoActivity";
    private EditText etDescription;
    private ImageView ivPostImage;
    private File photoFile;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Photos from " + event.getName());

        etDescription = findViewById(R.id.etDescription);
        Button btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        pb = findViewById(R.id.pbLoading);

        btnCaptureImage.setOnClickListener(v -> launchCamera());

        btnSubmit.setOnClickListener(v -> {
            pb.setVisibility(ProgressBar.VISIBLE);
            submitPhotoToServer();
        });
    }

    private void submitPhotoToServer() {
        String description = etDescription.getText().toString();
        String eventId = event.getId();
        if (description.isEmpty()) {
            Toast.makeText(AddPhotoActivity.this, "Description cannot be empty!", Toast.LENGTH_LONG).show();
            pb.setVisibility(ProgressBar.INVISIBLE);
            return;
        }
        if (photoFile == null || ivPostImage.getDrawable() == null) {
            Toast.makeText(AddPhotoActivity.this, "Photo cannot be empty!", Toast.LENGTH_LONG).show();
            pb.setVisibility(ProgressBar.INVISIBLE);
            return;
        }
        ParseUser currentUser = ParseUser.getCurrentUser();
        savePhoto(description, currentUser, photoFile, eventId);
    }

    private void savePhoto(String description, ParseUser currentUser, File photoFile, String eventId) {
        Photo photo = new Photo();
        photo.setEventId(eventId);
        photo.setDescription(description);
        photo.setImage(new ParseFile(photoFile));
        photo.setUser(currentUser);
        photo.saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(AddPhotoActivity.this, "Error saving post!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(AddPhotoActivity.this, "Posted!", Toast.LENGTH_LONG).show();
            etDescription.setText("");
            ivPostImage.setImageResource(0);
            pb.setVisibility(ProgressBar.INVISIBLE);
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFilePath(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(AddPhotoActivity.this, "com.codepath.event_photo_provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    private void resizePhoto(Bitmap resizedBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, bytes);
        File resizedFile = getPhotoFilePath(photoFileName + "_resized");
        try {
            resizedFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(resizedFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert fos != null;
            fos.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getPhotoFilePath(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Toast.makeText(AddPhotoActivity.this, "Failed to create photo directory", Toast.LENGTH_LONG).show();
        }
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(getPhotoFilePath(photoFileName));
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, PHOTO_WIDTH);
                resizePhoto(resizedBitmap);
                ivPostImage.setImageBitmap(resizedBitmap);
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
