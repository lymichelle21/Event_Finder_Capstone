package com.capstone.event_finder.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.capstone.event_finder.R;
import com.capstone.event_finder.utils.ZipFormatCheck;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SignUpActivity extends AppCompatActivity {

    public static final int PHOTO_PICKER_REQUEST_CODE = 2022;
    public String photoFileName = "profile.jpg";
    LottieAnimationView animatedConfetti;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etZip;
    private EditText etBio;
    private ImageButton ibProfileImage;
    private TextView tvEventCategoryDropdown;
    private String allInterestCategories;
    private ArrayList<String> categoryListOfStrings;
    private ParseFile file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etZip = findViewById(R.id.etZip);
        etBio = findViewById(R.id.etBio);
        ibProfileImage = findViewById(R.id.ibProfileImage);
        tvEventCategoryDropdown = findViewById(R.id.tvEventCategoryDropdown);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        animatedConfetti = findViewById(R.id.animatedConfetti);

        setUpEventCategoryDropdown();
        btnSignUp.setOnClickListener(v -> signUpUser());

        ibProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PHOTO_PICKER_REQUEST_CODE);
        });
    }

    public Bitmap loadImageFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
            image = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            Toast.makeText(SignUpActivity.this, "Error loading image uri", Toast.LENGTH_SHORT).show();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PICKER_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Uri currentUri = data.getData();
                Bitmap selectedImage = loadImageFromUri(currentUri);
                ibProfileImage.setImageBitmap(selectedImage);
                compressAndGetImageAsParseFile(selectedImage);
            } else {
                Toast.makeText(SignUpActivity.this, "Error opening gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void compressAndGetImageAsParseFile(Bitmap selectedImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] imageRec = stream.toByteArray();
        file = new ParseFile(photoFileName, imageRec);
        file.saveInBackground((SaveCallback) e -> {
            if (null != e) {
                Toast.makeText(SignUpActivity.this, "Error saving profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCheckAnimationLogo() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        animator.addUpdateListener(animation -> animatedConfetti.setProgress((Float) animation.getAnimatedValue()));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                goMainActivity();
            }
        });
        if (animatedConfetti.getProgress() == 0f) {
            animator.start();
        } else {
            animatedConfetti.setProgress(0f);
        }
    }

    private void setUpEventCategoryDropdown() {
        boolean[] selectedCategories;
        ArrayList<Integer> categoryList = new ArrayList<>();
        String[] categoryArray = {"music", "visual-arts", "performing-arts", "film",
                "lectures-books", "fashion", "food-and-drink", "festivals-fairs", "charities",
                "sports-active-life", "nightlife", "kids-family"};

        selectedCategories = new boolean[categoryArray.length];

        tvEventCategoryDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Select favorite event categories :");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(categoryArray, selectedCategories, (dialog, i, isCategoryChecked) -> getCategoryListBasedOnSelection(i, isCategoryChecked, categoryList));

                setUpSaveButton(builder);
                builder.show();
            }

            private void setUpSaveButton(AlertDialog.Builder builder) {
                builder.setPositiveButton("Save", (dialogInterface, i) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    categoryListOfStrings = new ArrayList<>();
                    for (i = 0; i < categoryList.size(); i++) {
                        String category = categoryArray[categoryList.get(i)];
                        stringBuilder.append(category);
                        categoryListOfStrings.add(category);
                        if (i != categoryList.size() - 1) {
                            stringBuilder.append(", ");
                        }
                    }
                    allInterestCategories = stringBuilder.toString();
                    tvEventCategoryDropdown.setText(allInterestCategories);
                });
            }
        });
    }

    private void getCategoryListBasedOnSelection(int i, boolean isCategoryChecked, ArrayList<Integer> categoryList) {
        if (isCategoryChecked) {
            categoryList.add(i);
            Collections.sort(categoryList);
        } else {
            categoryList.remove(Integer.valueOf(i));
        }
    }

    private void signUpUser() {
        if (file == null) {
            Toast.makeText(SignUpActivity.this, "Error: No profile image added", Toast.LENGTH_LONG).show();
        }

        if (!ZipFormatCheck.isZipValidFormat(etZip.getText().toString(), SignUpActivity.this)) {
            return;
        }

        if (categoryListOfStrings == null) {
            Toast.makeText(SignUpActivity.this, "Error: All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("zip", etZip.getText().toString());
        user.put("bio", etBio.getText().toString());
        user.put("event_categories", categoryListOfStrings);
        user.put("event_categories_string", allInterestCategories);
        user.put("profile_image", file);

        registerUserToParse(user);
    }

    private void registerUserToParse(ParseUser user) {
        user.signUpInBackground(e -> {
            if (e != null) {
                e.printStackTrace();
                if (e.getCode() == ParseException.USERNAME_TAKEN) {
                    Toast.makeText(SignUpActivity.this, "Error: Username already taken", Toast.LENGTH_LONG).show();
                    return;
                }
                e.printStackTrace();
                Toast.makeText(SignUpActivity.this, "Error: All fields are required", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SignUpActivity.this, "Sign up success!", Toast.LENGTH_LONG).show();
            animatedConfetti.playAnimation();
            startCheckAnimationLogo();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

