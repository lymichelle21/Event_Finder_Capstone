package com.capstone.event_finder.activities;

import android.content.Context;
import android.content.DialogInterface;
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

import com.capstone.event_finder.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etZip;
    private EditText etBio;
    private TextView tvEventCategoryDropdown;

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

                builder.setMultiChoiceItems(categoryArray, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isCategoryChecked) {
                        if (isCategoryChecked) {
                            categoryList.add(i);
                            Collections.sort(categoryList);
                        } else {
                            categoryList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < categoryList.size(); j++) {
                            stringBuilder.append(categoryArray[categoryList.get(j)]);
                            if (j != categoryList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        tvEventCategoryDropdown.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedCategories.length; j++) {
                            selectedCategories[j] = false;
                            categoryList.clear();
                            tvEventCategoryDropdown.setText("");
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        btnSignUp.setOnClickListener(v -> signUpUser());
    }

    private void signUpUser() {
        ParseUser user = new ParseUser();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("zip", etZip.getText().toString());
        user.put("bio", etBio.getText().toString());

        user.signUpInBackground(e -> {
            if (e != null) {
                Toast.makeText(SignUpActivity.this, "Error: Sign up failed", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SignUpActivity.this, "Sign up success!", Toast.LENGTH_LONG).show();
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

