package com.capstone.event_finder.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.capstone.event_finder.R;
import com.capstone.event_finder.utils.ZipFormatCheck;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;

public class SignUpActivity extends AppCompatActivity {

    LottieAnimationView animatedConfetti;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etZip;
    private EditText etBio;
    private TextView tvEventCategoryDropdown;
    private String allInterestCategories;
    private ArrayList<String> categoryListOfStrings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etZip = findViewById(R.id.etZip);
        etBio = findViewById(R.id.etBio);
        tvEventCategoryDropdown = findViewById(R.id.tvEventCategoryDropdown);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        animatedConfetti = findViewById(R.id.animatedConfetti);

        setUpEventCategoryDropdown();
        btnSignUp.setOnClickListener(v -> signUpUser());
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
        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("zip", etZip.getText().toString());
        user.put("bio", etBio.getText().toString());

        if (ZipFormatCheck.isZipValidFormat(etZip.getText().toString(), SignUpActivity.this) == false) {
            return;
        }
        if (categoryListOfStrings == null) {
            Toast.makeText(SignUpActivity.this, "Error: All fields are required", Toast.LENGTH_LONG).show();
            return;
        }
        user.put("event_categories", categoryListOfStrings);
        user.put("event_categories_string", allInterestCategories);
        registerUserToParse(user);
    }

    private void registerUserToParse(ParseUser user) {
        user.signUpInBackground(e -> {
            if (e != null) {
                if (e.getCode() == ParseException.USERNAME_TAKEN) {
                    Toast.makeText(SignUpActivity.this, "Error: Username already taken", Toast.LENGTH_LONG).show();
                    return;
                }
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

